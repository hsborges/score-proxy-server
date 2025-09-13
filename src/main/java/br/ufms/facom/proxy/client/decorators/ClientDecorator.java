package br.ufms.facom.proxy.client.decorators;

import br.ufms.facom.proxy.client.Client;

public abstract class ClientDecorator implements Client {
    protected final Client delegate;

    protected ClientDecorator(Client delegate) {
        this.delegate = delegate;
    }
}
