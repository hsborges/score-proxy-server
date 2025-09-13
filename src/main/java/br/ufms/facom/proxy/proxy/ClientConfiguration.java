package br.ufms.facom.proxy.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.ufms.facom.proxy.client.Client;
import br.ufms.facom.proxy.client.UpstreamClient;
import br.ufms.facom.proxy.client.decorators.CachingDecorator;
import br.ufms.facom.proxy.client.decorators.RateLimitingDecorator;
import br.ufms.facom.proxy.utils.AppProperties;
import br.ufms.facom.proxy.utils.cache.InMemoryCache;

@Configuration
public class ClientConfiguration {

    @Autowired
    private AppProperties props;

    @Bean 
    Client upstreamClient() {
        return new CachingDecorator(
            new RateLimitingDecorator(new UpstreamClient(props.getBaseUrl(), props.getClientId())), 
            new InMemoryCache<>()
        );
    }
}
