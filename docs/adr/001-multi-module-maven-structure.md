# ADR 001: Multi-Module Maven Structure

## Status

Accepted

## Context

The project includes both a CLI agent and an MCP server (quote-server). The MCP server communicates with the agent over STDIO, meaning it runs as a separate OS process. Both components share domain types (Quote, QuoteCategory).

## Decision

Use a multi-module Maven project with three modules:
- **quote-common**: Shared DTOs (Quote record, QuoteCategory enum)
- **quote-server**: Standalone MCP server packaged as a fat JAR
- **agent**: CLI agent application packaged as a fat JAR

## Consequences

### Positive
- Clean separation of concerns: server and agent have independent dependency trees
- Shared types prevent serialization mismatches between server and agent
- Each module can be built and tested independently
- Fat JARs simplify deployment — no classpath management needed

### Negative
- Slightly more complex build configuration (parent POM, shade plugin per module)
- Developers must run `mvn install` on quote-common before building dependent modules (though `mvn package` from root handles this)

## Alternatives Considered

1. **Single module**: Would couple agent and server dependencies, making the fat JAR unnecessarily large and creating potential classpath conflicts
2. **Two modules (server + agent)**: Would duplicate DTO classes or force the agent to depend on the server module
