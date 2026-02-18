package com.agent.cli.io;

public interface ConsoleIO {

    String readLine(String prompt);

    void println(String message);

    void printError(String message);

    boolean hasNextLine();
}
