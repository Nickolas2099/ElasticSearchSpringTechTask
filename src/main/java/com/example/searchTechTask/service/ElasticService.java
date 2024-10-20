package com.example.searchTechTask.service;

import com.example.searchTechTask.domain.response.Response;
import org.springframework.http.ResponseEntity;

public interface ElasticService {

    ResponseEntity<Response> uploadData();

    ResponseEntity<Response> createIndex();

    ResponseEntity<Response> searchByWord(String KeyWord);
}
