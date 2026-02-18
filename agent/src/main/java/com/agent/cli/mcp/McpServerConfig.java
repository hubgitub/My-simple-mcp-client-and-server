package com.agent.cli.mcp;

import com.agent.cli.i18n.Messages;

import java.util.List;
import java.util.Objects;

public record McpServerConfig(String name, List<String> command) {

    public McpServerConfig {
        Objects.requireNonNull(name, Messages.get("mcp.name.null"));
        Objects.requireNonNull(command, Messages.get("mcp.command.null"));
        if (command.isEmpty()) {
            throw new IllegalArgumentException(Messages.get("mcp.command.empty"));
        }
    }
}
