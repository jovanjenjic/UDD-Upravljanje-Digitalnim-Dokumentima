package com.example.udd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;


@SpringBootApplication
@EnableElasticsearchRepositories("com.example.udd.repository")
public class UddApplication {

	public static void main(String[] args) {
		SpringApplication.run(UddApplication.class, args);
	}

}
