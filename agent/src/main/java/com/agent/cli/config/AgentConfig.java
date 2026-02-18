package com.agent.cli.config;

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
        Objects.requireNonNull(modelName, "modelName must not be null");
        Objects.requireNonNull(ollamaBaseUrl, "ollamaBaseUrl must not be null");
        Objects.requireNonNull(chatMemoryConfig, "chatMemoryConfig must not be null");
        Objects.requireNonNull(mcpServerCommands, "mcpServerCommands must not be null");
    }
}
