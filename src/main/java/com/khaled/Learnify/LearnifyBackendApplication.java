package com.khaled.Learnify;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.khaled.Learnify.services.FilesStorageServiceImpl;

@SpringBootApplication
public class LearnifyBackendApplication implements CommandLineRunner {

	  @Resource
	  FilesStorageServiceImpl storageService;
	public static void main(String[] args) {
		SpringApplication.run(LearnifyBackendApplication.class, args);
	}

	  @Override
	  public void run(String... arg) throws Exception {
	    storageService.init();
	  }
}
