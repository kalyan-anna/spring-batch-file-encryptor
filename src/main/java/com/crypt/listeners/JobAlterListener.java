package com.crypt.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobAlterListener implements JobExecutionListener {

	private static final Logger logger = LogManager.getLogger(JobAlterListener.class);

	@Override
	public void beforeJob(JobExecution jobExecution) {
		logger.info("File Encryptor Job started");
		logger.info("File to encrypt {}", jobExecution.getJobParameters().getString("pathToInputFile"));
		logger.info("Number of thread to execute {}", jobExecution.getJobParameters().getString("numberOfThreads"));
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		logger.info("File Encryptor Job finished");
	}
}
