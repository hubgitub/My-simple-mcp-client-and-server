package com.agent.cli.config;

import java.util.ArrayList;
import java.util.List;

public class AgentConfigBuilder {

    private String modelName = AgentConfig.DEFAULT_MODEL;
    private String ollamaBaseUrl = AgentConfig.DEFAULT_OLLAMA_URL;
    private ChatMemoryConfig chatMemoryConfig = ChatMemoryConfig.defaultConfig();
    private final List<String> mcpServerCommands = new ArrayList<>();

    public AgentConfigBuilder modelName(String modelName) {
        this.modelName = modelName;
        return this;
    }

    public AgentConfigBuilder ollamaBaseUrl(String ollamaBaseUrl) {
        this.ollamaBaseUrl = ollamaBaseUrl;
        return this;
    }

    public AgentConfigBuilder chatMemoryConfig(ChatMemoryConfig chatMemoryConfig) {
        this.chatMemoryConfig = chatMemoryConfig;
        return this;
    }

    public AgentConfigBuilder addMcpServerCommand(String command) {
        this.mcpServerCommands.add(command);
        return this;
    }

    public AgentConfigBuilder mcpServerCommands(List<String> commands) {
        this.mcpServerCommands.clear();
        this.mcpServerCommands.addAll(commands);
        return this;
    }

    public AgentConfig build() {
        return new AgentConfig(modelName, ollamaBaseUrl, chatMemoryConfig, List.copyOf(mcpServerCommands));
    }
}
