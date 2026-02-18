package com.agent.cli.config;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AgentConfigBuilderTest {

    @Test
    void shouldBuildWithDefaults() {
        AgentConfig config = new AgentConfigBuilder().build();

        assertEquals(AgentConfig.DEFAULT_MODEL, config.modelName());
        assertEquals(AgentConfig.DEFAULT_OLLAMA_URL, config.ollamaBaseUrl());
        assertEquals(ChatMemoryConfig.DEFAULT_MAX_MESSAGES, config.chatMemoryConfig().maxMessages());
        assertTrue(config.mcpServerCommands().isEmpty());
    }

    @Test
    void shouldBuildWithCustomValues() {
        AgentConfig config = new AgentConfigBuilder()
                .modelName("mistral")
                .ollamaBaseUrl("http://remote:11434")
                .chatMemoryConfig(new ChatMemoryConfig(50))
                .addMcpServerCommand("java -jar server.jar")
                .build();

        assertEquals("mistral", config.modelName());
        assertEquals("http://remote:11434", config.ollamaBaseUrl());
        assertEquals(50, config.chatMemoryConfig().maxMessages());
        assertEquals(List.of("java -jar server.jar"), config.mcpServerCommands());
    }

    @Test
    void shouldSupportMultipleMcpServers() {
        AgentConfig config = new AgentConfigBuilder()
                .addMcpServerCommand("server1")
                .addMcpServerCommand("server2")
                .build();

        assertEquals(2, config.mcpServerCommands().size());
    }

    @Test
    void shouldReturnImmutableMcpServerList() {
        AgentConfig config = new AgentConfigBuilder()
                .addMcpServerCommand("server1")
                .build();

        assertThrows(UnsupportedOperationException.class,
                () -> config.mcpServerCommands().add("server2"));
    }

    @Test
    void shouldReplaceMcpServerCommands() {
        AgentConfig config = new AgentConfigBuilder()
                .addMcpServerCommand("old-server")
                .mcpServerCommands(List.of("new-server"))
                .build();

        assertEquals(List.of("new-server"), config.mcpServerCommands());
    }
}
