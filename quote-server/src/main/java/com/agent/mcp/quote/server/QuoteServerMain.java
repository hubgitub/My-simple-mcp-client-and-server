package com.agent.mcp.quote.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;

public class QuoteServerMain {

    public static void main(String[] args) {
        QuoteRepository repository = new InMemoryQuoteRepository();
        QuoteToolHandler toolHandler = new QuoteToolHandler(repository);

        StdioServerTransportProvider transport = new StdioServerTransportProvider(new ObjectMapper());

        McpSyncServer server = McpServer.sync(transport)
                .serverInfo("quote-server", "1.0.0")
                .capabilities(ServerCapabilities.builder()
                        .tools(true)
                        .build())
                .tools(toolHandler.createToolSpecifications())
                .build();

        Runtime.getRuntime().addShutdownHook(new Thread(server::closeGracefully));
    }
}
