package com.agent.cli.chat;

import com.agent.cli.event.ChatEvent;
import com.agent.cli.event.ChatEventListener;
import com.agent.cli.io.ConsoleIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatSessionTest {

    @Mock
    private AssistantService assistant;

    @Mock
    private ConsoleIO consoleIO;

    @Mock
    private ChatEventListener listener;

    private ChatSession session;

    @BeforeEach
    void setUp() {
        session = new ChatSession(assistant, consoleIO, List.of(listener));
    }

    @Test
    void shouldStartAndEndSessionOnExit() {
        when(consoleIO.hasNextLine()).thenReturn(true);
        when(consoleIO.readLine(anyString())).thenReturn("exit");

        session.start("test-model");

        ArgumentCaptor<ChatEvent> captor = ArgumentCaptor.forClass(ChatEvent.class);
        verify(listener, atLeast(2)).onEvent(captor.capture());

        List<ChatEvent> events = captor.getAllValues();
        assertInstanceOf(ChatEvent.SessionStarted.class, events.get(0));
        assertInstanceOf(ChatEvent.SessionEnded.class, events.get(events.size() - 1));
    }

    @Test
    void shouldHandleQuitCommand() {
        when(consoleIO.hasNextLine()).thenReturn(true);
        when(consoleIO.readLine(anyString())).thenReturn("quit");

        session.start("test-model");

        verify(listener, atLeast(1)).onEvent(any(ChatEvent.SessionEnded.class));
    }

    @Test
    void shouldSkipBlankInput() {
        when(consoleIO.hasNextLine()).thenReturn(true, true);
        when(consoleIO.readLine(anyString())).thenReturn("", "exit");

        session.start("test-model");

        verify(assistant, never()).chat(anyString());
    }

    @Test
    void shouldSendMessageToAssistant() {
        when(consoleIO.hasNextLine()).thenReturn(true, true);
        when(consoleIO.readLine(anyString())).thenReturn("hello", "exit");
        when(assistant.chat("hello")).thenReturn("Hi there!");

        session.start("test-model");

        verify(assistant).chat("hello");
    }

    @Test
    void shouldFireResponseEvent() {
        when(consoleIO.hasNextLine()).thenReturn(true, true);
        when(consoleIO.readLine(anyString())).thenReturn("hello", "exit");
        when(assistant.chat("hello")).thenReturn("Hi there!");

        session.start("test-model");

        ArgumentCaptor<ChatEvent> captor = ArgumentCaptor.forClass(ChatEvent.class);
        verify(listener, atLeast(3)).onEvent(captor.capture());

        boolean hasResponse = captor.getAllValues().stream()
                .anyMatch(e -> e instanceof ChatEvent.AssistantResponseReceived r
                        && "Hi there!".equals(r.response()));
        assertTrue(hasResponse);
    }

    @Test
    void shouldFireErrorEventOnException() {
        when(consoleIO.hasNextLine()).thenReturn(true, true);
        when(consoleIO.readLine(anyString())).thenReturn("hello", "exit");
        when(assistant.chat("hello")).thenThrow(new RuntimeException("LLM error"));

        session.start("test-model");

        ArgumentCaptor<ChatEvent> captor = ArgumentCaptor.forClass(ChatEvent.class);
        verify(listener, atLeast(3)).onEvent(captor.capture());

        boolean hasError = captor.getAllValues().stream()
                .anyMatch(e -> e instanceof ChatEvent.ErrorOccurred err
                        && "LLM error".equals(err.message()));
        assertTrue(hasError);
    }

    @Test
    void shouldEndSessionWhenNoMoreInput() {
        when(consoleIO.hasNextLine()).thenReturn(false);

        session.start("test-model");

        verify(listener).onEvent(any(ChatEvent.SessionStarted.class));
        verify(listener).onEvent(any(ChatEvent.SessionEnded.class));
    }
}
