package com.crypt;

import java.io.File;

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
    private JobLauncher jobLauncher;
      
    @Autowired
    private Job job;
    
    @Autowired
    public FileCryptApplication(ApplicationArguments args) {
		String pathToInputFile = args.getOptionValues("pathToInputFile").get(0);
		String numberOfThreads = args.getOptionValues("numberOfThreads").get(0);
		
		if (!numberOfThreads.matches("\\d+")) {
			throw new IllegalArgumentException("argument numberOfThreads is not a valid number");
		}
		
		int threadCount = Integer.parseInt(numberOfThreads);
		if (threadCount < 1 || threadCount > 20) {
			throw new IllegalArgumentException("argument numberOfThreads should be between 1 - 20");
		}
		
		File file = new File(pathToInputFile);
		if (!file.exists() || !file.isFile()) {
			throw new IllegalArgumentException("argument pathToInputFile is not a valid file");
		}
    }
    
	public static void main(String[] args) {
		SpringApplication.run(FileCryptApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		jobLauncher.run(job, new JobParametersBuilder().toJobParameters());
	}

}
