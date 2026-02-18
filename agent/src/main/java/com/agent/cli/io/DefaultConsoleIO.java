package com.agent.cli.io;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class DefaultConsoleIO implements ConsoleIO {

    private final Console console;
    private final BufferedReader fallbackReader;
    private final PrintStream out;
    private final PrintStream err;
    private boolean eof;

    public DefaultConsoleIO() {
        this.console = System.console();
        this.fallbackReader = (console == null)
                ? new BufferedReader(new InputStreamReader(System.in))
                : null;
        this.out = System.out;
        this.err = System.err;
        this.eof = false;
    }

    // Constructor for testing (no Console available)
    public DefaultConsoleIO(BufferedReader reader, PrintStream out, PrintStream err) {
        this.console = null;
        this.fallbackReader = reader;
        this.out = out;
        this.err = err;
        this.eof = false;
    }

    @Override
    public String readLine(String prompt) {
        if (console != null) {
            String line = console.readLine(prompt);
            if (line == null) {
                eof = true;
            }
            return line;
        }
        out.print(prompt);
        out.flush();
        try {
            String line = fallbackReader.readLine();
            if (line == null) {
                eof = true;
            }
            return line;
        } catch (IOException e) {
            eof = true;
            return null;
        }
    }

    @Override
    public void println(String message) {
        out.println(message);
    }

    @Override
    public void printError(String message) {
        err.println(message);
    }

    @Override
    public boolean hasNextLine() {
        return !eof;
    }
}
