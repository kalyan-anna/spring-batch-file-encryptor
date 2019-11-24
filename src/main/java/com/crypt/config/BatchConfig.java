package com.crypt.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.crypt.steps.FileWriter;
import com.crypt.steps.LineEncryptor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	@StepScope
	public FlatFileItemReader<String> reader() {
		FlatFileItemReader<String> itemReader = new FlatFileItemReader<String>();
		itemReader.setLineMapper(new PassThroughLineMapper());
		itemReader.setResource(new ClassPathResource("sample.txt"));
		return itemReader;
	}

	@Bean
	public Job job(Step step1) {
		return jobBuilderFactory.get("fileEncrypt").incrementer(new RunIdIncrementer()).start(step1).build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<String, String>chunk(2).reader(reader()).processor(processor())
				.writer(writer()).build();
	}

	@Bean
	public FileWriter writer() {
		return new FileWriter();
	}

	@Bean
	public LineEncryptor processor() {
		return new LineEncryptor();
	}
}
