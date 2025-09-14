package br.ufms.facom.proxy.client.decorators;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.ufms.facom.proxy.client.Client;
import br.ufms.facom.proxy.utils.Command;

public class RateLimitingDecorator extends ClientDecorator {
    private final BlockingQueue<Command<ResponseEntity<String>>> queue = new LinkedBlockingQueue<>();
    private final Thread worker;

    public RateLimitingDecorator(Client delegate) {
        super(delegate);
        worker = new Thread(this::processQueue, "RateLimitingClientWorker");
        worker.setDaemon(true);
        worker.start();
    }

    private void processQueue() {
        while (true) {
            try {
                Command<ResponseEntity<String>> command = queue.take();                
                ResponseEntity<String> response = command.execute().get();
                long ratelimitResetIn = Long.parseLong(response.getHeaders().get("x-ratelimit-reset-in").getFirst());
                if (ratelimitResetIn > 0) TimeUnit.MILLISECONDS.sleep(ratelimitResetIn);
                if (response.getStatusCode().isSameCodeAs(HttpStatus.TOO_MANY_REQUESTS)) this.queue.add(command);
            } catch (InterruptedException | ExecutionException ignored) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    @Override
    public ResponseEntity<String> getScore(String cpf) {
        try {
            Command<ResponseEntity<String>> command = new Command<>(() -> delegate.getScore(cpf));
            queue.put(command);
            return command.getFuture().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
