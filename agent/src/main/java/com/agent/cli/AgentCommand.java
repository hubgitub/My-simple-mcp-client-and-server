package com.agent.cli;

import com.agent.cli.chat.AssistantService;
import com.agent.cli.chat.AssistantServiceFactory;
import com.agent.cli.chat.ChatSession;
import com.agent.cli.config.AgentConfig;
import com.agent.cli.config.AgentConfigBuilder;
import com.agent.cli.event.ChatEventListener;
import com.agent.cli.event.ConsoleChatEventListener;
import com.agent.cli.io.ConsoleIO;
import com.agent.cli.io.DefaultConsoleIO;
import com.agent.cli.llm.ChatModelProvider;
import com.agent.cli.llm.ChatModelProviderFactory;
import com.agent.cli.mcp.DefaultMcpClientManager;
import com.agent.cli.mcp.McpClientManager;
import com.agent.cli.mcp.McpServerConfig;
import com.agent.cli.mcp.McpServerConfigParser;
import dev.langchain4j.mcp.client.McpClient;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@Command(
        name = "agent",
        mixinStandardHelpOptions = true,
        version = "1.0.0",
        description = "CLI agent using Ollama with MCP tool support"
)
public class AgentCommand implements Callable<Integer> {

    @Option(names = {"-m", "--model"}, description = "Ollama model name (default: ${DEFAULT-VALUE})",
            defaultValue = "qwen3")
    private String model;

    @Option(names = {"-u", "--url"}, description = "Ollama base URL (default: ${DEFAULT-VALUE})",
            defaultValue = "http://localhost:11434")
    private String ollamaUrl;

    @Option(names = {"--mcp-server"}, description = "MCP server command (repeatable)",
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
                io.println("Connected to " + mcpClients.size() + " MCP server(s)");
            }

            AssistantService assistant = serviceFactory.create(modelProvider, config, mcpClients);

            List<ChatEventListener> listeners = List.of(new ConsoleChatEventListener(io));
            ChatSession session = new ChatSession(assistant, io, listeners);
            session.start(modelProvider.getModelName());

            return 0;
        } catch (Exception e) {
            io.printError("Fatal error: " + e.getMessage());
            return 1;
        }
    }
}
