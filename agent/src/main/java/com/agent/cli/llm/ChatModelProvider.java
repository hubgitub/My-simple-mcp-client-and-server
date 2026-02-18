package com.agent.cli.llm;

import dev.langchain4j.model.chat.ChatLanguageModel;

public interface ChatModelProvider {

    ChatLanguageModel createChatModel();

    String getModelName();
}
