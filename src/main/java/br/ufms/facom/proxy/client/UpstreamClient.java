package br.ufms.facom.proxy.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class UpstreamClient implements Client {
    private final RestTemplate restTemplate = new RestTemplate();

    private final String baseUrl;
    private final String clientId;

    public UpstreamClient(String baseUrl, String clientId) {
        this.baseUrl = baseUrl;
        this.clientId = clientId;
    }

    @Override
    public ResponseEntity<String> getScore(String cpf) {
        String url = String.format("%s/score?cpf=%s", baseUrl, cpf);
        HttpHeaders headers = new HttpHeaders();
        headers.set("client-id", clientId);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            
            HttpHeaders filtered = new HttpHeaders();
            response.getHeaders().forEach((key, value) -> {
                if (key != null && key.toLowerCase().startsWith("x-ratelimit-")) {
                    filtered.put(key, value);
                }
            });
            return ResponseEntity.status(response.getStatusCode())
                    .headers(filtered)
                    .body(response.getBody());
        } catch (HttpClientErrorException e) {
            HttpHeaders filtered = new HttpHeaders();
            HttpHeaders errorHeaders = e.getResponseHeaders();
            if (errorHeaders != null) {
                errorHeaders.forEach((key, value) -> {
                    if (key != null && key.toLowerCase().startsWith("x-ratelimit-")) {
                        filtered.put(key, value);
                    }
                });
            }
            return ResponseEntity.status(e.getStatusCode())
                    .headers(filtered)
                    .body(e.getResponseBodyAsString());
        }
    }
}
