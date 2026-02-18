package com.agent.mcp.quote.server;

import com.agent.mcp.quote.common.Quote;
import com.agent.mcp.quote.common.QuoteCategory;

import java.util.List;

public interface QuoteRepository {

    Quote getRandomQuote();

    Quote getRandomQuoteByCategory(QuoteCategory category);

    List<Quote> getAllQuotes();

    List<Quote> getQuotesByCategory(QuoteCategory category);
}
