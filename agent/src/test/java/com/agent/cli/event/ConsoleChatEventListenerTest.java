package com.agent.cli.event;

import com.agent.cli.i18n.Messages;
import com.agent.cli.io.ConsoleIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsoleChatEventListenerTest {

    @Mock
    private ConsoleIO consoleIO;

    private ConsoleChatEventListener listener;

    @BeforeEach
    void setUp() {
        listener = new ConsoleChatEventListener(consoleIO);
    }

    @Test
    void shouldPrintSessionStartedMessage() {
        listener.onEvent(new ChatEvent.SessionStarted("llama3.2"));
        verify(consoleIO).println(contains("llama3.2"));
    }

    @Test
    void shouldPrintAssistantResponse() {
        listener.onEvent(new ChatEvent.AssistantResponseReceived("Hello!"));
        verify(consoleIO).println(contains("Hello!"));
    }

    @Test
    void shouldPrintErrorMessage() {
        listener.onEvent(new ChatEvent.ErrorOccurred("Something failed", new RuntimeException()));
        verify(consoleIO).printError(contains("Something failed"));
    }

    @Test
    void shouldPrintGoodbyeOnSessionEnd() {
        listener.onEvent(new ChatEvent.SessionEnded());
        verify(consoleIO).println(Messages.get("event.session.ended"));
    }

    @Test
    void shouldNotPrintForUserMessage() {
        listener.onEvent(new ChatEvent.UserMessageReceived("hello"));
        verifyNoInteractions(consoleIO);
    }
}
