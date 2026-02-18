package com.agent.mcp.quote.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuoteCategoryTest {

    @Test
    void shouldHaveFourCategories() {
        assertEquals(4, QuoteCategory.values().length);
    }

    @Test
    void shouldContainExpectedValues() {
        assertNotNull(QuoteCategory.valueOf("INSPIRATION"));
        assertNotNull(QuoteCategory.valueOf("HUMOR"));
        assertNotNull(QuoteCategory.valueOf("WISDOM"));
        assertNotNull(QuoteCategory.valueOf("MOTIVATION"));
    }

    @Test
    void shouldRejectInvalidCategory() {
        assertThrows(IllegalArgumentException.class,
                () -> QuoteCategory.valueOf("INVALID"));
    }
}
