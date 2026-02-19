package com.agent.cli;

import com.agent.cli.chat.AssistantService;
import com.agent.cli.chat.AssistantServiceFactory;
import com.agent.cli.chat.ChatSession;
import com.agent.cli.config.AgentConfig;
import com.agent.cli.config.AgentConfigBuilder;
import com.agent.cli.event.ChatEventListener;
import com.agent.cli.event.ConsoleChatEventListener;
import com.agent.cli.i18n.Messages;
import com.agent.cli.io.ConsoleIO;
import com.agent.cli.io.DefaultConsoleIO;
import com.agent.cli.llm.ChatModelProvider;
import com.agent.cli.llm.ChatModelProviderFactory;
import com.agent.cli.mcp.DefaultMcpClientManager;
import com.agent.cli.mcp.McpClientManager;
import com.agent.cli.mcp.McpServerConfig;
import com.agent.cli.mcp.McpServerConfigParser;
import dev.langchain4j.mcp.client.McpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@Command(
        name = "agent",
        mixinStandardHelpOptions = true,
        version = "1.0.0",
        resourceBundle = "messages"
)
public class AgentCommand implements Callable<Integer> {

    private static final Logger log = LoggerFactory.getLogger(AgentCommand.class);

    @Option(names = {"-m", "--model"}, descriptionKey = "cli.option.model",
            defaultValue = "qwen3")
    private String model;

    @Option(names = {"-u", "--url"}, descriptionKey = "cli.option.url",
            defaultValue = "http://localhost:11434")
    private String ollamaUrl;

    @Option(names = {"--mcp-server"}, descriptionKey = "cli.option.mcp",
            arity = "1")
    private List<String> mcpServers = new ArrayList<>();

    // Visible for testing
    ConsoleIO consoleIO;
    ChatModelProviderFactory modelProviderFactory;
    McpClientManager mcpClientManager;
    AssistantServiceFactory assistantServiceFactory;

    @Override
    public Integer call() {
        AgentConfig config = new AgentConfigBuilder()
                .modelName(model)
                .ollamaBaseUrl(ollamaUrl)
                .mcpServerCommands(mcpServers != null ? mcpServers : Collections.emptyList())
                .build();

        validateOllamaUrl(config.ollamaBaseUrl());

        ConsoleIO io = consoleIO != null ? consoleIO : new DefaultConsoleIO();
        ChatModelProviderFactory providerFactory = modelProviderFactory != null
                ? modelProviderFactory : new ChatModelProviderFactory();
        McpClientManager clientManager = mcpClientManager != null
                ? mcpClientManager : new DefaultMcpClientManager();
        AssistantServiceFactory serviceFactory = assistantServiceFactory != null
                ? assistantServiceFactory : new AssistantServiceFactory();

        try (clientManager) {
            ChatModelProvider modelProvider = providerFactory.create(config);

            List<McpClient> mcpClients = List.of();
            if (!config.mcpServerCommands().isEmpty()) {
                McpServerConfigParser parser = new McpServerConfigParser();
                List<McpServerConfig> serverConfigs = parser.parseAll(config.mcpServerCommands());
                mcpClients = clientManager.createClients(serverConfigs);
                io.println(Messages.format("agent.connected", mcpClients.size()));
            }

            AssistantService assistant = serviceFactory.create(modelProvider, config, mcpClients);

            List<ChatEventListener> listeners = List.of(new ConsoleChatEventListener(io));
            ChatSession session = new ChatSession(assistant, io, listeners);
            session.start(modelProvider.getModelName());

            return 0;
        } catch (Exception e) {
            log.debug("Fatal error", e);
            io.printError(Messages.get("agent.fatal.error.generic"));
            return 1;
        }
    }

    private void validateOllamaUrl(String url) {
        try {
            URI uri = URI.create(url);
            String host = uri.getHost();
            String scheme = uri.getScheme();
            boolean isLocal = "localhost".equalsIgnoreCase(host)
                    || "127.0.0.1".equals(host) || "::1".equals(host);
            if ("http".equalsIgnoreCase(scheme) && !isLocal) {
                log.warn(Messages.format("agent.url.insecure", url));
            }
        } catch (IllegalArgumentException e) {
            log.warn(Messages.format("agent.url.invalid", url));
        }
    }
}
