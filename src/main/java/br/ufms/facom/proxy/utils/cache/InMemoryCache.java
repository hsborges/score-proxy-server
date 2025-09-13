package br.ufms.facom.proxy.utils.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCache<K, V> implements Cache<K, V> {
    private final Map<K, V> map = new ConcurrentHashMap<>();

    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public void put(K key, V value) {
        map.put(key, value);
    }

    @Override
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    @Override
    public void clear() {
        map.clear();
    }
}
