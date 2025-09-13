package br.ufms.facom.proxy.utils.cache;

public interface Cache<K, V> {
    V get(K key);
    void put(K key, V value);
    boolean containsKey(K key);
    void clear();
}
