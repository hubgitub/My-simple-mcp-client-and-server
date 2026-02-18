package com.agent.cli.llm;

import com.agent.cli.config.AgentConfig;
import com.agent.cli.config.AgentConfigBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatModelProviderFactoryTest {

    @Test
    void shouldCreateOllamaProvider() {
        AgentConfig config = new AgentConfigBuilder()
                .modelName("mistral")
                .ollamaBaseUrl("http://localhost:11434")
                .build();

        ChatModelProviderFactory factory = new ChatModelProviderFactory();
        ChatModelProvider provider = factory.create(config);

        assertNotNull(provider);
        assertInstanceOf(OllamaChatModelProvider.class, provider);
        assertEquals("mistral", provider.getModelName());
    }

    @Test
    void shouldCreateProviderWithDefaultConfig() {
        AgentConfig config = new AgentConfigBuilder().build();

        ChatModelProviderFactory factory = new ChatModelProviderFactory();
        ChatModelProvider provider = factory.create(config);

        assertEquals(AgentConfig.DEFAULT_MODEL, provider.getModelName());
    }
}
