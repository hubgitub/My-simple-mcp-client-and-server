package com.agent.mcp.quote.server;

import com.agent.mcp.quote.common.Quote;
import com.agent.mcp.quote.common.QuoteCategory;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class InMemoryQuoteRepository implements QuoteRepository {

    private final List<Quote> quotes;
    private final Random random;

    public InMemoryQuoteRepository() {
        this(new Random());
    }

    public InMemoryQuoteRepository(Random random) {
        this.random = random;
        this.quotes = List.of(
                // INSPIRATION
                new Quote("The only way to do great work is to love what you do.", "Steve Jobs", QuoteCategory.INSPIRATION),
                new Quote("Innovation distinguishes between a leader and a follower.", "Steve Jobs", QuoteCategory.INSPIRATION),
                new Quote("The future belongs to those who believe in the beauty of their dreams.", "Eleanor Roosevelt", QuoteCategory.INSPIRATION),
                new Quote("It is during our darkest moments that we must focus to see the light.", "Aristotle", QuoteCategory.INSPIRATION),
                new Quote("Creativity is intelligence having fun.", "Albert Einstein", QuoteCategory.INSPIRATION),

                // HUMOR
                new Quote("I'm not superstitious, but I am a little stitious.", "Michael Scott", QuoteCategory.HUMOR),
                new Quote("Behind every great man is a woman rolling her eyes.", "Jim Carrey", QuoteCategory.HUMOR),
                new Quote("I used to think I was indecisive. But now I'm not so sure.", "Tommy Cooper", QuoteCategory.HUMOR),
                new Quote("A day without sunshine is like, you know, night.", "Steve Martin", QuoteCategory.HUMOR),
                new Quote("I'm writing a book. I've got the page numbers done.", "Steven Wright", QuoteCategory.HUMOR),

                // WISDOM
                new Quote("The only true wisdom is in knowing you know nothing.", "Socrates", QuoteCategory.WISDOM),
                new Quote("In the middle of difficulty lies opportunity.", "Albert Einstein", QuoteCategory.WISDOM),
                new Quote("The unexamined life is not worth living.", "Socrates", QuoteCategory.WISDOM),
                new Quote("Knowing yourself is the beginning of all wisdom.", "Aristotle", QuoteCategory.WISDOM),
                new Quote("Turn your wounds into wisdom.", "Oprah Winfrey", QuoteCategory.WISDOM),

                // MOTIVATION
                new Quote("It does not matter how slowly you go as long as you do not stop.", "Confucius", QuoteCategory.MOTIVATION),
                new Quote("Success is not final, failure is not fatal: it is the courage to continue that counts.", "Winston Churchill", QuoteCategory.MOTIVATION),
                new Quote("Believe you can and you're halfway there.", "Theodore Roosevelt", QuoteCategory.MOTIVATION),
                new Quote("The secret of getting ahead is getting started.", "Mark Twain", QuoteCategory.MOTIVATION),
                new Quote("Don't watch the clock; do what it does. Keep going.", "Sam Levenson", QuoteCategory.MOTIVATION)
        );
    }

    @Override
    public Quote getRandomQuote() {
        return quotes.get(random.nextInt(quotes.size()));
    }

    @Override
    public Quote getRandomQuoteByCategory(QuoteCategory category) {
        List<Quote> filtered = getQuotesByCategory(category);
        if (filtered.isEmpty()) {
            throw new IllegalArgumentException("No quotes found for category: " + category);
        }
        return filtered.get(random.nextInt(filtered.size()));
    }

    @Override
    public List<Quote> getAllQuotes() {
        return quotes;
    }

    @Override
    public List<Quote> getQuotesByCategory(QuoteCategory category) {
        return quotes.stream()
                .filter(q -> q.category() == category)
                .collect(Collectors.toList());
    }
}
