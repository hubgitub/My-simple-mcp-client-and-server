package com.agent.mcp.quote.common;

import com.agent.mcp.quote.common.i18n.Messages;

import java.util.Objects;

public record Quote(String text, String author, QuoteCategory category) {

    public Quote {
        Objects.requireNonNull(text, Messages.get("quote.text.null"));
        Objects.requireNonNull(author, Messages.get("quote.author.null"));
        Objects.requireNonNull(category, Messages.get("quote.category.null"));
        if (text.isBlank()) {
            throw new IllegalArgumentException(Messages.get("quote.text.blank"));
        }
        if (author.isBlank()) {
            throw new IllegalArgumentException(Messages.get("quote.author.blank"));
        }
    }
}
