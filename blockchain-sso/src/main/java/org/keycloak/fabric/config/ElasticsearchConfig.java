package org.keycloak.fabric.config;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@RequiredArgsConstructor
public class ElasticsearchConfig {

    //elasticsearch propertiesi çağırdık
    private final ElasticsearchProperties elasticsearchProperties;

    //elastic searche bağlanır
    @Bean
    Client client() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", elasticsearchProperties.getClusterName())
                .build();
        TransportClient client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(new TransportAddress(InetAddress.getByName(elasticsearchProperties.getHost())
                , elasticsearchProperties.getPort()));
        return client;
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws UnknownHostException {
        return new ElasticsearchTemplate(client());
    }

}