package com.agent.cli.event;

import com.agent.cli.io.ConsoleIO;

public class ConsoleChatEventListener implements ChatEventListener {

    private final ConsoleIO consoleIO;

    public ConsoleChatEventListener(ConsoleIO consoleIO) {
        this.consoleIO = consoleIO;
    }

    @Override
    public void onEvent(ChatEvent event) {
        switch (event) {
            case ChatEvent.SessionStarted e ->
                    consoleIO.println("Agent started with model: " + e.modelName()
                            + "\nType 'exit' or 'quit' to end the session.\n");
            case ChatEvent.AssistantResponseReceived e ->
                    consoleIO.println("\nAssistant: " + e.response() + "\n");
            case ChatEvent.ErrorOccurred e ->
                    consoleIO.printError("Error: " + e.message());
            case ChatEvent.SessionEnded e ->
                    consoleIO.println("Goodbye!");
            case ChatEvent.UserMessageReceived e -> {
                // No console output needed for user messages
            }
        }
    }
}
