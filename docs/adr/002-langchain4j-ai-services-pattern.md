# ADR 002: LangChain4j AI Services Pattern

## Status

Accepted

## Context

The agent needs to orchestrate LLM calls, manage chat memory, and integrate MCP tools. LangChain4j offers two approaches:
1. Manual orchestration using `ChatLanguageModel` directly with explicit tool handling loops
2. AI Services — a declarative, interface-based approach using `AiServices.builder()`

## Decision

Use the AI Services pattern for building the assistant. Define an `AssistantService` interface annotated with `@SystemMessage`, and let `AiServices` handle tool invocation, memory management, and response parsing.

```java
public interface AssistantService {
    @SystemMessage("...")
    String chat(String userMessage);
}
```

## Consequences

### Positive
- Minimal boilerplate: no manual tool dispatch loop, no message list management
- Automatic tool integration via `ToolProvider` (MCP tools discovered and executed transparently)
- Chat memory injected declaratively via builder
- Easy to test: the interface can be mocked in unit tests
- Follows LangChain4j idiomatic patterns, reducing maintenance burden

### Negative
- Less control over the tool invocation loop (e.g., cannot intercept between tool call and tool result)
- Debugging tool calls requires logging configuration rather than stepping through code
- Tight coupling to LangChain4j abstractions

## Alternatives Considered

1. **Manual orchestration**: More flexible but requires implementing the tool call loop, message history management, and error handling manually — significantly more code with higher bug surface
