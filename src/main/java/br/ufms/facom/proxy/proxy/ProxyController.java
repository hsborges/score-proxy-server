package br.ufms.facom.proxy.proxy;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import br.ufms.facom.proxy.client.Client;

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
    private Client client;

    @Operation(
        summary = "Get score by CPF",
        description = "Proxies a request to another server to retrieve the score for the given CPF."
    )
    @GetMapping("/score")
    public ResponseEntity<String> getScore(
            @Parameter(description = "CPF of the user", required = true)
            @RequestParam String cpf) {
        
        return client.getScore(cpf);
    }
}
