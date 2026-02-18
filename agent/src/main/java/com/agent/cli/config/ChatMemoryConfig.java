package com.agent.cli.config;

public record ChatMemoryConfig(int maxMessages) {

    public static final int DEFAULT_MAX_MESSAGES = 20;

    public ChatMemoryConfig {
        if (maxMessages < 1) {
            throw new IllegalArgumentException("maxMessages must be at least 1");
        }
    }

    public static ChatMemoryConfig defaultConfig() {
        return new ChatMemoryConfig(DEFAULT_MAX_MESSAGES);
    }
}
