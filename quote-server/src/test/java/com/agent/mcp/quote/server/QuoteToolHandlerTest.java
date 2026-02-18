package com.agent.mcp.quote.server;

import com.agent.mcp.quote.common.Quote;
import com.agent.mcp.quote.common.QuoteCategory;
import com.agent.mcp.quote.server.i18n.Messages;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuoteToolHandlerTest {

    @Mock
    private QuoteRepository repository;

    private QuoteToolHandler handler;

    @BeforeEach
    void setUp() {
        handler = new QuoteToolHandler(repository);
    }

    @Test
    void shouldCreateTwoToolSpecifications() {
        List<SyncToolSpecification> specs = handler.createToolSpecifications();
        assertEquals(2, specs.size());
    }

    @Test
    void shouldHaveCorrectToolNames() {
        List<SyncToolSpecification> specs = handler.createToolSpecifications();
        assertEquals("get_random_quote", specs.get(0).tool().name());
        assertEquals("get_quote_by_category", specs.get(1).tool().name());
    }

    @Test
    void shouldReturnRandomQuote() {
        Quote expected = new Quote("Test quote", "Test Author", QuoteCategory.WISDOM);
        when(repository.getRandomQuote()).thenReturn(expected);

        List<SyncToolSpecification> specs = handler.createToolSpecifications();
        SyncToolSpecification randomQuoteTool = specs.get(0);

        CallToolResult result = randomQuoteTool.call().apply(null, Map.of());

        assertFalse(result.isError());
        assertFalse(result.content().isEmpty());
        String text = ((TextContent) result.content().get(0)).text();
        assertTrue(text.contains("Test quote"));
        assertTrue(text.contains("Test Author"));
    }

    @Test
    void shouldReturnQuoteByCategory() {
        Quote expected = new Quote("Funny quote", "Comedian", QuoteCategory.HUMOR);
        when(repository.getRandomQuoteByCategory(QuoteCategory.HUMOR)).thenReturn(expected);

        List<SyncToolSpecification> specs = handler.createToolSpecifications();
        SyncToolSpecification categoryTool = specs.get(1);

        CallToolResult result = categoryTool.call().apply(null, Map.of("category", "HUMOR"));

        assertFalse(result.isError());
        String text = ((TextContent) result.content().get(0)).text();
        assertTrue(text.contains("Funny quote"));
    }

    @Test
    void shouldReturnErrorForInvalidCategory() {
        List<SyncToolSpecification> specs = handler.createToolSpecifications();
        SyncToolSpecification categoryTool = specs.get(1);

        CallToolResult result = categoryTool.call().apply(null, Map.of("category", "INVALID"));

        assertTrue(result.isError());
        String text = ((TextContent) result.content().get(0)).text();
        String categories = Arrays.stream(QuoteCategory.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
        String expected = Messages.format("quote.invalid.category", "INVALID", categories);
        assertEquals(expected, text);
    }
}
