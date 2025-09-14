package br.ufms.facom.proxy;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/")
@Tag(name = "Home")
public class HomeController {

    @Operation(
        summary = "Health Check",
        description = "Endpoint to check if the service is alive."
    )
    @GetMapping("/health")
    public ResponseEntity<String> liveness() {
        return ResponseEntity.ok("I'm alive");
    }

}
