package br.ufms.facom.proxy.client;

import org.springframework.http.ResponseEntity;

public interface Client {
    ResponseEntity<String> getScore(String cpf);
}
