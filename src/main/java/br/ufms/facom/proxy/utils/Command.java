package br.ufms.facom.proxy.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public class Command<T> {
    private final Callable<T> action;
    private final CompletableFuture<T> future = new CompletableFuture<>();

    public Command(Callable<T> action) {
        this.action = action;
    }

    public void execute() {
        try {
            T result = action.call();
            future.complete(result);
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
    }

    public CompletableFuture<T> getFuture() {
        return future;
    }
}
