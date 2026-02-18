package com.agent.mcp.quote.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuoteTest {

    @Test
    void shouldCreateValidQuote() {
        Quote quote = new Quote("Be yourself.", "Oscar Wilde", QuoteCategory.WISDOM);

        assertEquals("Be yourself.", quote.text());
        assertEquals("Oscar Wilde", quote.author());
        assertEquals(QuoteCategory.WISDOM, quote.category());
    }

    @Test
    void shouldRejectNullText() {
        assertThrows(NullPointerException.class,
                () -> new Quote(null, "Author", QuoteCategory.HUMOR));
    }

    @Test
    void shouldRejectNullAuthor() {
        assertThrows(NullPointerException.class,
                () -> new Quote("Text", null, QuoteCategory.HUMOR));
    }

    @Test
    void shouldRejectNullCategory() {
        assertThrows(NullPointerException.class,
                () -> new Quote("Text", "Author", null));
    }

    @Test
    void shouldRejectBlankText() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quote("   ", "Author", QuoteCategory.HUMOR));
    }

    @Test
    void shouldRejectBlankAuthor() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quote("Text", "   ", QuoteCategory.HUMOR));
    }

    @Test
    void shouldSupportEquality() {
        Quote q1 = new Quote("Test", "Author", QuoteCategory.INSPIRATION);
        Quote q2 = new Quote("Test", "Author", QuoteCategory.INSPIRATION);

        assertEquals(q1, q2);
        assertEquals(q1.hashCode(), q2.hashCode());
    }
}
