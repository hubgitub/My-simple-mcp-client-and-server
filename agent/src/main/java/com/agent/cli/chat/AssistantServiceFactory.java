package com.agent.cli.chat;

import com.agent.cli.config.AgentConfig;
import com.agent.cli.llm.ChatModelProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AssistantServiceFactory {

    private static final Logger log = LoggerFactory.getLogger(AssistantServiceFactory.class);

    public AssistantService create(ChatModelProvider modelProvider, AgentConfig config, List<McpClient> mcpClients) {
        ChatLanguageModel chatModel = modelProvider.createChatModel();

        AiServices<AssistantService> builder = AiServices.builder(AssistantService.class)
                .chatLanguageModel(chatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(
                        config.chatMemoryConfig().maxMessages()));

        if (!mcpClients.isEmpty()) {
            McpToolProvider toolProvider = McpToolProvider.builder()
                    .mcpClients(mcpClients)
                    .build();
            log.info("Registering MCP tool provider with {} client(s). " +
                    "The LLM will autonomously decide when to invoke tools.", mcpClients.size());
            builder.toolProvider(toolProvider);
        }

        return builder.build();
    }
}
