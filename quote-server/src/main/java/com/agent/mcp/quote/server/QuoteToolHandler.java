package com.agent.mcp.quote.server;

import com.agent.mcp.quote.common.Quote;
import com.agent.mcp.quote.common.QuoteCategory;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import io.modelcontextprotocol.spec.McpSchema.Tool;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QuoteToolHandler {

    private final QuoteRepository repository;

    public QuoteToolHandler(QuoteRepository repository) {
        this.repository = repository;
    }

    public List<SyncToolSpecification> createToolSpecifications() {
        return List.of(
                createGetRandomQuoteTool(),
                createGetQuoteByCategoryTool()
        );
    }

    private SyncToolSpecification createGetRandomQuoteTool() {
        String schema = """
                {
                  "type": "object",
                  "properties": {},
                  "required": []
                }
                """;

        return new SyncToolSpecification(
                new Tool("get_random_quote", "Get a random quote from any category", schema),
                (exchange, arguments) -> {
                    Quote quote = repository.getRandomQuote();
                    return formatQuoteResult(quote);
                }
        );
    }

    private SyncToolSpecification createGetQuoteByCategoryTool() {
        String categories = Arrays.stream(QuoteCategory.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));

        String schema = """
                {
                  "type": "object",
                  "properties": {
                    "category": {
                      "type": "string",
                      "description": "The quote category. Valid values: %s",
                      "enum": [%s]
                    }
                  },
                  "required": ["category"]
                }
                """.formatted(
                categories,
                Arrays.stream(QuoteCategory.values())
                        .map(c -> "\"" + c.name() + "\"")
                        .collect(Collectors.joining(", "))
        );

        return new SyncToolSpecification(
                new Tool("get_quote_by_category", "Get a random quote from a specific category", schema),
                (exchange, arguments) -> {
                    String categoryStr = (String) arguments.get("category");
                    try {
                        QuoteCategory category = QuoteCategory.valueOf(categoryStr.toUpperCase());
                        Quote quote = repository.getRandomQuoteByCategory(category);
                        return formatQuoteResult(quote);
                    } catch (IllegalArgumentException e) {
                        return new CallToolResult(
                                List.of(new TextContent("Invalid category: " + categoryStr
                                        + ". Valid categories are: " + categories)),
                                true
                        );
                    }
                }
        );
    }

    private CallToolResult formatQuoteResult(Quote quote) {
        String text = "\"%s\" — %s [%s]".formatted(
                quote.text(), quote.author(), quote.category().name());
        return new CallToolResult(List.of(new TextContent(text)), false);
    }
}
