package com.agent.cli.event;

public sealed interface ChatEvent {

    record SessionStarted(String modelName) implements ChatEvent {}

    record UserMessageReceived(String message) implements ChatEvent {}

    record AssistantResponseReceived(String response) implements ChatEvent {}

    record ErrorOccurred(String message, Throwable cause) implements ChatEvent {}

    record SessionEnded() implements ChatEvent {}
}
