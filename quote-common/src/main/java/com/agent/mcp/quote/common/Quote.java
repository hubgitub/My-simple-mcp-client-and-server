package com.agent.mcp.quote.common;

import java.util.Objects;

public record Quote(String text, String author, QuoteCategory category) {

    public Quote {
        Objects.requireNonNull(text, "text must not be null");
        Objects.requireNonNull(author, "author must not be null");
        Objects.requireNonNull(category, "category must not be null");
        if (text.isBlank()) {
            throw new IllegalArgumentException("text must not be blank");
        }
        if (author.isBlank()) {
            throw new IllegalArgumentException("author must not be blank");
        }
    }
}
