package com.agent.cli.chat;

import com.agent.cli.event.ChatEvent;
import com.agent.cli.event.ChatEventListener;
import com.agent.cli.i18n.Messages;
import com.agent.cli.io.ConsoleIO;

import java.util.List;
import java.util.Set;

public class ChatSession {

    private static final Set<String> EXIT_COMMANDS = Set.of("exit", "quit", "bye");
    private static final String PROMPT = Messages.get("session.prompt");

    private final AssistantService assistant;
    private final ConsoleIO consoleIO;
    private final List<ChatEventListener> listeners;

    public ChatSession(AssistantService assistant, ConsoleIO consoleIO, List<ChatEventListener> listeners) {
        this.assistant = assistant;
        this.consoleIO = consoleIO;
        this.listeners = listeners;
    }

    public void start(String modelName) {
        fireEvent(new ChatEvent.SessionStarted(modelName));

        while (consoleIO.hasNextLine()) {
            String input = consoleIO.readLine(PROMPT);

            if (input == null || EXIT_COMMANDS.contains(input.trim().toLowerCase())) {
                fireEvent(new ChatEvent.SessionEnded());
                return;
            }

            if (input.isBlank()) {
                continue;
            }

            fireEvent(new ChatEvent.UserMessageReceived(input));

            try {
                String response = assistant.chat(input);
                fireEvent(new ChatEvent.AssistantResponseReceived(response));
            } catch (Exception e) {
                fireEvent(new ChatEvent.ErrorOccurred(e.getMessage(), e));
            }
        }

        fireEvent(new ChatEvent.SessionEnded());
    }

    private void fireEvent(ChatEvent event) {
        for (ChatEventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}
