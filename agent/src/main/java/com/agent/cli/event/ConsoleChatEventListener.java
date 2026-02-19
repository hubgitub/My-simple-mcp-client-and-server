package com.agent.cli.event;

import com.agent.cli.i18n.Messages;
import com.agent.cli.io.ConsoleIO;
import com.agent.cli.io.OutputSanitizer;

public class ConsoleChatEventListener implements ChatEventListener {

    private final ConsoleIO consoleIO;

    public ConsoleChatEventListener(ConsoleIO consoleIO) {
        this.consoleIO = consoleIO;
    }

    @Override
    public void onEvent(ChatEvent event) {
        switch (event) {
            case ChatEvent.SessionStarted e ->
                    consoleIO.println(Messages.format("event.session.started", e.modelName()));
            case ChatEvent.AssistantResponseReceived e ->
                    consoleIO.println(Messages.format("event.assistant.response",
                            OutputSanitizer.stripAnsiEscapes(e.response())));
            case ChatEvent.ErrorOccurred e ->
                    consoleIO.printError(Messages.format("event.error", e.message()));
            case ChatEvent.SessionEnded e ->
                    consoleIO.println(Messages.get("event.session.ended"));
            case ChatEvent.UserMessageReceived e -> {
                // No console output needed for user messages
            }
        }
    }
}
