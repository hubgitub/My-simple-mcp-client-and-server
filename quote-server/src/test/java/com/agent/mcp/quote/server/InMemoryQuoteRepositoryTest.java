package com.agent.mcp.quote.server;

import com.agent.mcp.quote.common.Quote;
import com.agent.mcp.quote.common.QuoteCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryQuoteRepositoryTest {

    private InMemoryQuoteRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryQuoteRepository(new Random(42));
    }

    @Test
    void shouldReturnRandomQuote() {
        Quote quote = repository.getRandomQuote();
        assertNotNull(quote);
        assertNotNull(quote.text());
        assertNotNull(quote.author());
        assertNotNull(quote.category());
    }

    @Test
    void shouldReturnAllQuotes() {
        List<Quote> quotes = repository.getAllQuotes();
        assertTrue(quotes.size() >= 20);
    }

    @Test
    void shouldReturnQuotesByCategory() {
        for (QuoteCategory category : QuoteCategory.values()) {
            List<Quote> quotes = repository.getQuotesByCategory(category);
            assertFalse(quotes.isEmpty(), "Should have quotes for " + category);
            quotes.forEach(q -> assertEquals(category, q.category()));
        }
    }

    @Test
    void shouldReturnRandomQuoteByCategory() {
        Quote quote = repository.getRandomQuoteByCategory(QuoteCategory.HUMOR);
        assertNotNull(quote);
        assertEquals(QuoteCategory.HUMOR, quote.category());
    }

    @Test
    void shouldHaveQuotesInAllCategories() {
        for (QuoteCategory category : QuoteCategory.values()) {
            List<Quote> quotes = repository.getQuotesByCategory(category);
            assertTrue(quotes.size() >= 4,
                    "Should have at least 4 quotes for " + category + ", found " + quotes.size());
        }
    }
}
