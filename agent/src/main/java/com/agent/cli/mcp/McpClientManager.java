package com.agent.cli.mcp;

import dev.langchain4j.mcp.client.McpClient;

import java.util.List;

public interface McpClientManager extends AutoCloseable {

    List<McpClient> createClients(List<McpServerConfig> serverConfigs);

    @Override
    void close();
}
