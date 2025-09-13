package br.ufms.facom.proxy.client.decorators;

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
            return cache.get(cpf);
        }
        ResponseEntity<String> response = delegate.getScore(cpf);
        cache.put(cpf, response);
        return response;
    }
}
