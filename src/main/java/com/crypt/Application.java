package com.crypt;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements ApplicationRunner {

	private static final Logger logger = LogManager.getLogger(Application.class);
	
	@Autowired
    private JobLauncher jobLauncher;
      
    @Autowired
    private Job job;
    
    @Autowired
    public Application(ApplicationArguments args) {
		String pathToInputFile = args.getOptionValues("pathToInputFile").get(0);
		String numberOfThreads = args.getOptionValues("numberOfThreads").get(0);
		
		if (!numberOfThreads.matches("\\d+")) {
			logger.error("Invalid numberOfThreads argument {}", numberOfThreads);
			throw new IllegalArgumentException("argument numberOfThreads is not a valid number");
		}
		
		int threadCount = Integer.parseInt(numberOfThreads);
		if (threadCount < 1 || threadCount > 20) {
			logger.error("numberOfThreads should be between 1 - 20 but received {}", numberOfThreads);
			throw new IllegalArgumentException("argument numberOfThreads should be between 1 - 20");
		}
		
		if (pathToInputFile == null) {
			logger.error("argument pathToInputFile is required");
			throw new IllegalArgumentException("argument pathToInputFile is required");
		}
		
		File file = new File(pathToInputFile);
		if (!file.exists() || !file.isFile()) {
			logger.error("argument pathToInputFile is not a valid file {}", pathToInputFile);
			throw new IllegalArgumentException("argument pathToInputFile is not a valid file");
		}
    }
    
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		jobLauncher.run(job, new JobParametersBuilder()
					.addString("pathToInputFile", args.getOptionValues("pathToInputFile").get(0))
					.addString("numberOfThreads", args.getOptionValues("numberOfThreads").get(0))
					.toJobParameters());
	}

}
