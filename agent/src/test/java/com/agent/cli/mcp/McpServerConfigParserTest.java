package com.agent.cli.mcp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class McpServerConfigParserTest {

    private McpServerConfigParser parser;

    @BeforeEach
    void setUp() {
        parser = new McpServerConfigParser();
    }

    @Test
    void shouldParseSimpleCommand() {
        McpServerConfig config = parser.parse("java -jar server.jar");

        assertEquals(List.of("java", "-jar", "server.jar"), config.command());
        assertEquals("server", config.name());
    }

    @Test
    void shouldParseCommandWithPath() {
        McpServerConfig config = parser.parse("java -jar /path/to/quote-server-1.0.0-SNAPSHOT.jar");

        assertEquals("quote-server-1.0.0-SNAPSHOT", config.name());
        assertEquals(3, config.command().size());
    }

    @Test
    void shouldParseSingleCommand() {
        McpServerConfig config = parser.parse("my-server");

        assertEquals(List.of("my-server"), config.command());
        assertEquals("my-server", config.name());
    }

    @Test
    void shouldHandleQuotedArguments() {
        McpServerConfig config = parser.parse("node \"my script.js\"");

        assertEquals(List.of("node", "my script.js"), config.command());
    }

    @Test
    void shouldRejectNullCommand() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(null));
    }

    @Test
    void shouldRejectBlankCommand() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse("   "));
    }

    @Test
    void shouldParseMultipleCommands() {
        List<McpServerConfig> configs = parser.parseAll(List.of(
                "java -jar server1.jar",
                "python server2.py"
        ));

        assertEquals(2, configs.size());
        assertEquals("server1", configs.get(0).name());
        assertEquals("server2", configs.get(1).name());
    }
}
