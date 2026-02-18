package com.agent.cli.config;

import com.agent.cli.i18n.Messages;

import java.util.List;
import java.util.Objects;

public record AgentConfig(
        String modelName,
        String ollamaBaseUrl,
        ChatMemoryConfig chatMemoryConfig,
        List<String> mcpServerCommands
) {
    public static final String DEFAULT_MODEL = "qwen3";
    public static final String DEFAULT_OLLAMA_URL = "http://localhost:11434";

    public AgentConfig {
        Objects.requireNonNull(modelName, Messages.get("config.modelName.null"));
        Objects.requireNonNull(ollamaBaseUrl, Messages.get("config.ollamaBaseUrl.null"));
        Objects.requireNonNull(chatMemoryConfig, Messages.get("config.chatMemoryConfig.null"));
        Objects.requireNonNull(mcpServerCommands, Messages.get("config.mcpServerCommands.null"));
    }
}
