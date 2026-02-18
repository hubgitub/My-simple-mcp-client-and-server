package com.agent.cli.mcp;

import com.agent.cli.i18n.Messages;

import java.util.ArrayList;
import java.util.List;

public class McpServerConfigParser {

    public McpServerConfig parse(String commandString) {
        if (commandString == null || commandString.isBlank()) {
            throw new IllegalArgumentException(Messages.get("mcp.command.null.or.blank"));
        }

        List<String> parts = splitCommand(commandString.trim());
        String name = extractServerName(parts);
        return new McpServerConfig(name, parts);
    }

    public List<McpServerConfig> parseAll(List<String> commandStrings) {
        return commandStrings.stream()
                .map(this::parse)
                .toList();
    }

    private List<String> splitCommand(String command) {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        char quoteChar = 0;

        for (int i = 0; i < command.length(); i++) {
            char c = command.charAt(i);

            if (inQuotes) {
                if (c == quoteChar) {
                    inQuotes = false;
                } else {
                    current.append(c);
                }
            } else if (c == '"' || c == '\'') {
                inQuotes = true;
                quoteChar = c;
            } else if (Character.isWhitespace(c)) {
                if (!current.isEmpty()) {
                    parts.add(current.toString());
                    current = new StringBuilder();
                }
            } else {
                current.append(c);
            }
        }

        if (!current.isEmpty()) {
            parts.add(current.toString());
        }

        return parts;
    }

    private String extractServerName(List<String> commandParts) {
        String executable = commandParts.get(commandParts.size() > 1
                ? commandParts.size() - 1
                : 0);
        // Extract filename without path and extension
        int lastSlash = executable.lastIndexOf('/');
        int lastBackslash = executable.lastIndexOf('\\');
        int start = Math.max(lastSlash, lastBackslash) + 1;
        String name = executable.substring(start);
        int dot = name.lastIndexOf('.');
        if (dot > 0) {
            name = name.substring(0, dot);
        }
        return name;
    }
}
