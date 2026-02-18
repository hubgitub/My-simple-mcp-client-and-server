package com.agent.cli.config;

import com.agent.cli.i18n.Messages;

public record ChatMemoryConfig(int maxMessages) {

    public static final int DEFAULT_MAX_MESSAGES = 20;

    public ChatMemoryConfig {
        if (maxMessages < 1) {
            throw new IllegalArgumentException(Messages.get("config.maxMessages.min"));
        }
    }

    public static ChatMemoryConfig defaultConfig() {
        return new ChatMemoryConfig(DEFAULT_MAX_MESSAGES);
    }
}
