package com.agent.cli.mcp;

import java.util.List;
import java.util.Objects;

public record McpServerConfig(String name, List<String> command) {

    public McpServerConfig {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(command, "command must not be null");
        if (command.isEmpty()) {
            throw new IllegalArgumentException("command must not be empty");
        }
    }
}
