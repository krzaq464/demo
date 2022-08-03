package com.example.demo;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@RestController
	public class TestCtrl {

		@Value("${app.test}")
		private String test;

		@PostConstruct
		public void init() {
			System.out.println("app.test = " + test);
		}

		@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<String> test() {
			var b = "{\"resultCode\": 1, \"message\": \"tedst error\"}";
			return ResponseEntity.badRequest().body(b);
		}
	}

}
