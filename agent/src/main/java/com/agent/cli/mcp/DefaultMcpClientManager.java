package com.agent.cli.mcp;

import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class DefaultMcpClientManager implements McpClientManager {

    private final List<McpClient> clients = new ArrayList<>();
    private final Duration toolExecutionTimeout;

    public DefaultMcpClientManager() {
        this(Duration.ofSeconds(60));
    }

    public DefaultMcpClientManager(Duration toolExecutionTimeout) {
        this.toolExecutionTimeout = toolExecutionTimeout;
    }

    @Override
    public List<McpClient> createClients(List<McpServerConfig> serverConfigs) {
        for (McpServerConfig config : serverConfigs) {
            McpTransport transport = new StdioMcpTransport.Builder()
                    .command(config.command())
                    .logEvents(false)
                    .build();

            McpClient client = new DefaultMcpClient.Builder()
                    .transport(transport)
                    .toolExecutionTimeout(toolExecutionTimeout)
                    .build();

            clients.add(client);
        }
        return List.copyOf(clients);
    }

    @Override
    public void close() {
        for (McpClient client : clients) {
            try {
                client.close();
            } catch (Exception e) {
                // Log and continue closing remaining clients
                System.err.println("Error closing MCP client: " + e.getMessage());
            }
        }
        clients.clear();
    }
}
