# Ollama MCP Agent

A Java CLI agent that uses **Ollama** for local LLM inference, **LangChain4j** for orchestration, **PicoCLI** for the command-line interface, and **MCP (Model Context Protocol)** for tool integration.

Includes a sample MCP server that provides random quotes.

## Architecture

```
┌────────────────────────────────────────────────┐
│                   Agent                        │
│  ┌──────────┐  ┌────────────┐  ┌──────────┐    │
│  │ PicoCLI  │→ │ LangChain4j│→ │  Ollama  │    │
│  │ (CLI)    │  │ AiServices │  │  (LLM)   │    │
│  └──────────┘  └─────┬──────┘  └──────────┘    │
│                      │                         │
│               ┌──────┴──────┐                  │
│               │McpToolProvider│                │
│               └──────┬──────┘                  │
└──────────────────────┼─────────────────────────┘
                       │ STDIO
              ┌────────┴────────┐
              │  Quote Server   │
              │  (MCP Server)   │
              └─────────────────┘
```

## Prerequisites

- **Java 21** (LTS)
- **Maven 3.x**
- **Ollama** installed and running with a model pulled (e.g., `ollama pull llama3.2`)

## Build

```bash
cd ollama-mcp-agent
mvn clean package
```

This builds all three modules and produces fat JARs for the agent and quote-server.

## Usage

### Run agent without MCP (direct chat with Ollama)

```bash
java -jar agent/target/agent-1.0.0-SNAPSHOT.jar
```

### Run agent with the quote MCP server

```bash
java -jar agent/target/agent-1.0.0-SNAPSHOT.jar \
  --mcp-server "java -jar quote-server/target/quote-server-1.0.0-SNAPSHOT.jar"
```

Then ask for quotes:
```
You: Give me an inspirational quote
Assistant: "The future belongs to those who believe in the beauty of their dreams." — Eleanor Roosevelt [INSPIRATION]
```

### CLI Options

| Option | Short | Description | Default |
|--------|-------|-------------|---------|
| `--model` | `-m` | Ollama model name | `llama3.2` |
| `--url` | `-u` | Ollama base URL | `http://localhost:11434` |
| `--mcp-server` | | MCP server command (repeatable) | none |
| `--help` | `-h` | Show help | |
| `--version` | `-V` | Show version | |

### Examples

Use a different model:
```bash
java -jar agent/target/agent-1.0.0-SNAPSHOT.jar -m mistral
```

Connect to a remote Ollama instance:
```bash
java -jar agent/target/agent-1.0.0-SNAPSHOT.jar -u http://192.168.1.100:11434
```

Multiple MCP servers:
```bash
java -jar agent/target/agent-1.0.0-SNAPSHOT.jar \
  --mcp-server "java -jar quote-server/target/quote-server-1.0.0-SNAPSHOT.jar" \
  --mcp-server "python my-other-server.py"
```

## Project Structure

```
ollama-mcp-agent/
├── pom.xml                  # Parent POM with dependency management
├── quote-common/            # Shared DTOs (Quote, QuoteCategory)
├── quote-server/            # MCP server providing random quotes
│   └── QuoteServerMain      #   Entry point (STDIO transport)
│   └── QuoteToolHandler     #   Tool definitions
│   └── InMemoryQuoteRepository  # 20 hardcoded quotes
└── agent/                   # CLI agent application
    ├── AgentCommand          #   PicoCLI entry point (Facade)
    ├── config/               #   Configuration (Builder pattern)
    ├── llm/                  #   LLM abstraction (Strategy pattern)
    ├── mcp/                  #   MCP client management
    ├── chat/                 #   Chat session and AI Services
    ├── event/                #   Event system (Observer pattern)
    └── io/                   #   Console I/O abstraction
```

## Design Patterns

| Pattern | Where | Why |
|---------|-------|-----|
| Strategy | `ChatModelProvider` | Swappable LLM backends |
| Factory | `ChatModelProviderFactory`, `AssistantServiceFactory` | Centralized object creation |
| Builder | `AgentConfigBuilder` | Complex immutable config construction |
| Observer | `ChatEventListener` / `ChatEvent` | Decoupled event handling |
| Repository | `QuoteRepository` / `InMemoryQuoteRepository` | Abstracted data access |
| Facade | `AgentCommand.run()` | Orchestrates startup sequence |

## How to Extend

### Add a new LLM provider

1. Implement `ChatModelProvider` interface
2. Update `ChatModelProviderFactory` to return your provider based on config

### Add a new MCP server

1. Create your server using the Java MCP SDK (see `quote-server` as reference)
2. Pass it via `--mcp-server "command to start your server"`

### Add new tools to the quote server

1. Add a new `SyncToolSpecification` in `QuoteToolHandler`
2. The agent will discover it automatically via MCP

## Technology Stack

|   Library  | Version |   Purpose   |
|------------|---------|-------------|
| Java       | 21      | Language    |
| Maven      | 3.x     | Build tool  |
| LangChain4j| 1.0.0-b2| LLM orches. |
| PicoCLI    | 4.7.7   | CLI parsing |
| J MCP SD K | 0.10.0  | MCP server  |
| JUnit 5    | 5.11.3  | Testing     |
| Mockito    | 5.14.2  | Mocking     |

## ADR Documents

- [001 - Multi-Module Maven Structure](docs/adr/001-multi-module-maven-structure.md)
- [002 - LangChain4j AI Services Pattern](docs/adr/002-langchain4j-ai-services-pattern.md)
- [003 - STDIO MCP Transport](docs/adr/003-stdio-mcp-transport.md)
