package com.example.searchTechTask;

import com.example.searchTechTask.service.ElasticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SearchTechTaskApplication implements CommandLineRunner {

	@Autowired
	private ElasticService elasticService;

	public static void main(String[] args) {
		SpringApplication.run(SearchTechTaskApplication.class, args);
	}

	@Override
	public void run(String ... args) {
		elasticService.createIndex();
	}



}
