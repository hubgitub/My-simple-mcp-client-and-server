package com.agent.cli.llm;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

import java.time.Duration;

public class OllamaChatModelProvider implements ChatModelProvider {

    static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(120);

    private final String modelName;
    private final String baseUrl;

    public OllamaChatModelProvider(String modelName, String baseUrl) {
        this.modelName = modelName;
        this.baseUrl = baseUrl;
    }

    @Override
    public ChatLanguageModel createChatModel() {
        return OllamaChatModel.builder()
                .baseUrl(baseUrl)
                .modelName(modelName)
                .timeout(DEFAULT_TIMEOUT)
                .build();
    }

    @Override
    public String getModelName() {
        return modelName;
    }
}
