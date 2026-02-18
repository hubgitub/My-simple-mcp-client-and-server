package com.agent.cli;

import picocli.CommandLine;

public class AgentMain {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new AgentCommand()).execute(args);
        System.exit(exitCode);
    }
}
