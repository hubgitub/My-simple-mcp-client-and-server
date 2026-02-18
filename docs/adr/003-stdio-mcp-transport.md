# ADR 003: STDIO MCP Transport

## Status

Accepted

## Context

The MCP specification supports multiple transport mechanisms:
1. **STDIO**: Server runs as a subprocess, communication via stdin/stdout
2. **SSE (Server-Sent Events)**: HTTP-based, server pushes events to client
3. **Streamable HTTP**: HTTP-based bidirectional streaming

The quote-server is a simple, single-purpose MCP server that will typically run on the same machine as the agent.

## Decision

Use STDIO transport for MCP communication. The agent launches the quote-server as a subprocess and communicates via stdin/stdout pipes.

## Consequences

### Positive
- Simplest deployment model: no HTTP server, no port management, no networking concerns
- Process lifecycle managed by the agent — server starts and stops with the agent
- No network security concerns (no open ports, no authentication needed)
- Works offline and in air-gapped environments
- LangChain4j's `StdioMcpTransport` handles subprocess management automatically

### Negative
- Server must run on the same machine as the agent (no remote servers)
- Cannot share a single server instance between multiple agent processes
- Debugging is harder — cannot use HTTP tools like curl to test the server independently
- Server crashes require agent restart

## Alternatives Considered

1. **SSE transport**: Better for remote/shared servers but adds HTTP server dependency, port configuration, and security concerns that are unnecessary for a local demo
2. **Streamable HTTP**: Most flexible but most complex to set up, overkill for this use case
