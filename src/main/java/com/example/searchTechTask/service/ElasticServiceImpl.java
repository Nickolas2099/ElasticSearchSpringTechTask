package com.example.searchTechTask.service;

import com.example.searchTechTask.domain.api.ProductDocument;
import com.example.searchTechTask.domain.api.SkuDocument;
import com.example.searchTechTask.domain.constant.Code;
import com.example.searchTechTask.domain.entity.Product;
import com.example.searchTechTask.domain.entity.Sku;
import com.example.searchTechTask.domain.response.Response;
import com.example.searchTechTask.domain.response.SuccessResponse;
import com.example.searchTechTask.domain.response.error.Error;
import com.example.searchTechTask.domain.response.error.ErrorResponse;
import com.example.searchTechTask.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ElasticServiceImpl implements ElasticService {

    private final ProductRepository productRepository;
    private final RestClient elasticClient;
    @Value("${elasticsearch.index}")
    private String indexName;
    private final ObjectMapper objectMapper;


    @Override
    public ResponseEntity<Response> uploadData() {
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            if (!product.isActive()) {
                continue;
            }
            List<SkuDocument> skuDocuments = new ArrayList<>();
            for (Sku sku : product.getSkus()) {
                SkuDocument skuDocument = SkuDocument.builder()
                        .skuId(sku.getId())
                        .skuColor(sku.getColor())
                        .skuSize(sku.getSize())
                        .skuPrice(sku.getPrice())
                        .build();
                skuDocuments.add(skuDocument);
            }

            ProductDocument responseDoc = ProductDocument.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .productActive(product.isActive())
                    .productStartDate(product.getStartDate())
                    .skus(skuDocuments)
                    .build();

            try {
                Request headRequest = new Request("HEAD", "/" + indexName + "/_doc/" + product.getId());
                var headResponse = elasticClient.performRequest(headRequest);
                log.info("ENTITY PRODUCT: {}", responseDoc);
                if (headResponse.getStatusLine().getStatusCode() == 404) {

                    String json = objectMapper.writeValueAsString(responseDoc);
                    HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);

                    Request indexRequest = new Request("PUT", "/" + indexName + "/_doc/"
                            + product.getId());
                    indexRequest.setEntity(entity);

                    elasticClient.performRequest(indexRequest);
                    log.info("New document with id: `{}`", product.getId());
                } else {
                    log.info("Document with id: `{}` already exists.", product.getId());
                }
            } catch (IOException ex) {
                log.error("The document has not been formed: `{}`", ex.getMessage());
                return new ResponseEntity<>(ErrorResponse.builder()
                        .error(Error.builder()
                                .code(Code.INTERNAL_SERVER_ERROR)
                                .build()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(SuccessResponse.builder().build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> createIndex() {
        try {
            Request request = new Request("PUT", "/" + indexName);
            request.setJsonEntity("{\"settings\":{\"number_of_shards\":3,\"number_of_replicas\":2}}");
            var response = elasticClient.performRequest(request);
            log.info("Index creation response: " + EntityUtils.toString(response.getEntity()));
            return new ResponseEntity<>(SuccessResponse.builder().build(), HttpStatus.OK);
        } catch (IOException ex) {
            log.warn("Index could not be created: `{}`. `{}`, `{}`", indexName, ex.getMessage(), ex.toString());
                return new ResponseEntity<>(ErrorResponse.builder()
                        .error(Error.builder()
                                .code(Code.INTERNAL_SERVER_ERROR)
                                .build()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Response> searchByWord(String searchWord) {
        try {
            Request request = new Request("GET", "/" + indexName + "/_search");

            Map<String, Object> query = Map.of(
                    "query", Map.of(
                            "bool", Map.of(
                                    "should", List.of(
                                            Map.of("match", Map.of("productName", searchWord)),
                                            Map.of("match", Map.of("skus.skuColor", searchWord))
                                    ), "minimum_should_match", 1
                            )
                    )
            );

            String json = objectMapper.writeValueAsString(query);
            HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
            request.setEntity(entity);
            var response = elasticClient.performRequest(request);

            String responseBody = EntityUtils.toString(response.getEntity());
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            List<Map<String, Object>> hits = new ArrayList<>();
            JsonNode hitsNode = jsonNode.path("hits").path("hits");
            for (JsonNode hit : hitsNode) {
                Map<String, Object> hitData = new HashMap<>();
                hitData.put("id", hit.path("_id").asText());
                hitData.put("score", hit.path("_score").asDouble());
                hitData.put("source", hit.path("_source"));
                hits.add(hitData);
            }

            return new ResponseEntity<>(SuccessResponse.builder()
                    .data(hits)
                    .build(), HttpStatus.OK);
        } catch(JsonProcessingException ex) {
            log.error("Error json assembling: `{}`", ex.getMessage());
            return new ResponseEntity<>(ErrorResponse.builder()
                    .error(Error.builder().code(Code.INTERNAL_SERVER_ERROR).build())
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException ex) {
            log.error("Error request sending: {}", ex.getMessage());
            return new ResponseEntity<>(ErrorResponse.builder()
                    .error(Error.builder().code(Code.INTERNAL_SERVER_ERROR).build())
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
