package com.example.searchTechTask.controller;

import com.example.searchTechTask.domain.response.Response;
import com.example.searchTechTask.service.ElasticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/search/{searchWord}")
    public ResponseEntity<Response> searchByKeyWord(@PathVariable("searchWord") String searchWord) {

        log.info("START endpoint searchByKeyWord, searchWord: `{}`", searchWord);
        ResponseEntity<Response> response = elasticService.searchByWord(searchWord);
        log.info("END endpoint searchWord");
        return response;
    }


}
