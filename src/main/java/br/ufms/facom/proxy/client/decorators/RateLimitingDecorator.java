package br.ufms.facom.proxy.client.decorators;

import org.springframework.http.ResponseEntity;

import br.ufms.facom.proxy.client.Client;
import br.ufms.facom.proxy.utils.Command;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class RateLimitingDecorator extends ClientDecorator {
    private final BlockingQueue<Command<?>> queue = new LinkedBlockingQueue<>();
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
                Command<?> command = queue.take();
                command.execute();
                TimeUnit.SECONDS.sleep(1); // 1 request per second
            } catch (InterruptedException ignored) {
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
            return command.getFuture().join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
