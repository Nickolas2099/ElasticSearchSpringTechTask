package com.example.searchTechTask.controller;

import com.example.searchTechTask.domain.response.Response;
import com.example.searchTechTask.service.ElasticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/elastic")
@Slf4j
@RequiredArgsConstructor
public class ElasticController {

    private final ElasticService elasticService;

    @PostMapping("/sync")
    public ResponseEntity<Response> synchronizeDbs() {

        log.info("START endpoint synchronizeDbs");
        ResponseEntity<Response> response = elasticService.uploadData();
        log.info("END endpoint synchronizeDbs");
        return response;
    }



}
