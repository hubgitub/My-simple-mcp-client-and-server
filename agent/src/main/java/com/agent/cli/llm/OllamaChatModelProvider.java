package com.agent.cli.llm;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

public class OllamaChatModelProvider implements ChatModelProvider {

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
                .build();
    }

    @Override
    public String getModelName() {
        return modelName;
    }
}
