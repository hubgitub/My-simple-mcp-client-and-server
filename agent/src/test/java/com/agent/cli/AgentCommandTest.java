package com.agent.cli;

import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.*;

class AgentCommandTest {

    @Test
    void shouldParseDefaultOptions() {
        AgentCommand command = new AgentCommand();
        new CommandLine(command).parseArgs();

        // Defaults are set via annotation defaultValue
        assertEquals("qwen3", getFieldValue(command, "model"));
        assertEquals("http://localhost:11434", getFieldValue(command, "ollamaUrl"));
    }

    @Test
    void shouldParseModelOption() {
        AgentCommand command = new AgentCommand();
        new CommandLine(command).parseArgs("--model", "mistral");

        assertEquals("mistral", getFieldValue(command, "model"));
    }

    @Test
    void shouldParseShortModelOption() {
        AgentCommand command = new AgentCommand();
        new CommandLine(command).parseArgs("-m", "codellama");

        assertEquals("codellama", getFieldValue(command, "model"));
    }

    @Test
    void shouldParseUrlOption() {
        AgentCommand command = new AgentCommand();
        new CommandLine(command).parseArgs("--url", "http://remote:11434");

        assertEquals("http://remote:11434", getFieldValue(command, "ollamaUrl"));
    }

    @Test
    void shouldParseShortUrlOption() {
        AgentCommand command = new AgentCommand();
        new CommandLine(command).parseArgs("-u", "http://remote:11434");

        assertEquals("http://remote:11434", getFieldValue(command, "ollamaUrl"));
    }

    @Test
    void shouldParseMcpServerOption() {
        AgentCommand command = new AgentCommand();
        new CommandLine(command).parseArgs("--mcp-server", "java -jar server.jar");

        @SuppressWarnings("unchecked")
        java.util.List<String> servers = (java.util.List<String>) getFieldValue(command, "mcpServers");
        assertEquals(1, servers.size());
        assertEquals("java -jar server.jar", servers.get(0));
    }

    @Test
    void shouldParseMultipleMcpServers() {
        AgentCommand command = new AgentCommand();
        new CommandLine(command).parseArgs(
                "--mcp-server", "server1",
                "--mcp-server", "server2"
        );

        @SuppressWarnings("unchecked")
        java.util.List<String> servers = (java.util.List<String>) getFieldValue(command, "mcpServers");
        assertEquals(2, servers.size());
    }

    @Test
    void shouldShowVersion() {
        CommandLine cmd = new CommandLine(new AgentCommand());
        int exitCode = cmd.execute("--version");
        assertEquals(0, exitCode);
    }

    private Object getFieldValue(AgentCommand command, String fieldName) {
        try {
            var field = AgentCommand.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(command);
        } catch (Exception e) {
            fail("Could not access field: " + fieldName);
            return null;
        }
    }
}
