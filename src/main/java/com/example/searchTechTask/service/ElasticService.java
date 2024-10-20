package com.example.searchTechTask.service;

import com.example.searchTechTask.domain.response.Response;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface ElasticService {

    public ResponseEntity<Response> uploadData();

    public ResponseEntity<Response> createIndex();
}
