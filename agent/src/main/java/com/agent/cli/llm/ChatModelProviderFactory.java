package com.agent.cli.llm;

import com.agent.cli.config.AgentConfig;

public class ChatModelProviderFactory {

    public ChatModelProvider create(AgentConfig config) {
        return new OllamaChatModelProvider(config.modelName(), config.ollamaBaseUrl());
    }
}
