package com.agent.cli.chat;

import dev.langchain4j.service.SystemMessage;

public interface AssistantService {

    @SystemMessage("""
            You are a helpful AI assistant. Answer questions clearly and concisely.
            When you have access to tools, use them when appropriate to provide better answers.
            """)
    String chat(String userMessage);
}
