package br.ufms.facom.proxy.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "proxy")
@Validated
@Getter
@Setter
public class AppProperties {
    /**
     * Client ID to be sent in the header of proxied requests.
     */
    @NotBlank(message = "proxy.client-id must not be empty")
    private String clientId;

    @NotBlank(message = "proxy.base-url must not be empty")
    private String baseUrl;
}
