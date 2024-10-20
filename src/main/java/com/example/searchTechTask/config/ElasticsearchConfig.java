package com.example.searchTechTask.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ElasticsearchConfig {

    @Value("${elasticsearch.host}")
    private String uri;


    @Bean(destroyMethod = "close")
    public RestClient elasticClient() {
        String[] uris = uri.split(":");
        RestClientBuilder builder = RestClient.builder(new HttpHost(uris[0], Integer.parseInt(uris[1]),
                "http"));
        return builder.build();
    }


}
