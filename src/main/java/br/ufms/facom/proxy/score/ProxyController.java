package br.ufms.facom.proxy.score;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.ufms.facom.proxy.utils.AppProperties;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/proxy")
@Tag(name = "Proxy", description = "API to proxy score requests to another server.")
public class ProxyController {

    @Autowired
    private AppProperties proxyProperties;

    @Operation(
        summary = "Get score by CPF",
        description = "Proxies a request to another server to retrieve the score for the given CPF."
    )
    @GetMapping("/score")
    public ResponseEntity<String> getScore(
            @Parameter(description = "CPF of the user", required = true)
            @RequestParam String cpf) {
        
        String url = String.format("%s/score?cpf=%s", proxyProperties.getBaseUrl(), cpf);

        HttpHeaders headers = new HttpHeaders();
        headers.set("client-id", proxyProperties.getClientId());

        try {
            ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Proxy error: " + e.getMessage());
        }
    }
}
