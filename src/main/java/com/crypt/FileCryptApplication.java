package com.crypt;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FileCryptApplication implements ApplicationRunner {

	@Autowired
    JobLauncher jobLauncher;
      
    @Autowired
    Job job;
    
	public static void main(String[] args) {
		SpringApplication.run(FileCryptApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		String pathToInputFile = args.getOptionValues("pathToInputFile").get(0);
		String numberOfThreads = args.getOptionValues("numberOfThreads").get(0);
		
		System.out.println("pathToInputFile:" + pathToInputFile);
		System.out.println("numberOfThreads:" + numberOfThreads);
        
		jobLauncher.run(job, new JobParametersBuilder().toJobParameters());
	}

}
