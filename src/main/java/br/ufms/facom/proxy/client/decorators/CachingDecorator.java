package br.ufms.facom.proxy.client.decorators;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import br.ufms.facom.proxy.client.Client;
import br.ufms.facom.proxy.utils.cache.Cache;
import br.ufms.facom.proxy.utils.cache.InMemoryCache;

public class CachingDecorator extends ClientDecorator {
    private final Cache<String, ResponseEntity<String>> cache;

    public CachingDecorator(Client delegate) {
        this(delegate, new InMemoryCache<>());
    }

    public CachingDecorator(Client delegate, Cache<String, ResponseEntity<String>> cache) {
        super(delegate);
        this.cache = cache;
    }

    @Override
    public ResponseEntity<String> getScore(String cpf) {
        if (cache.containsKey(cpf)) {
            ResponseEntity<String> cached = cache.get(cpf);
            HttpHeaders newHeaders = new HttpHeaders();
            newHeaders.putAll(cached.getHeaders());
            newHeaders.set("x-ratelimit-reset", String.valueOf(System.currentTimeMillis() / 1000));
            newHeaders.set("x-ratelimit-reset-in", "0");
            return new ResponseEntity<>(cached.getBody(), newHeaders, cached.getStatusCode());
        }
        ResponseEntity<String> response = delegate.getScore(cpf);
        if (response.getStatusCode().is2xxSuccessful()) cache.put(cpf, response);
        return response;
    }
}
