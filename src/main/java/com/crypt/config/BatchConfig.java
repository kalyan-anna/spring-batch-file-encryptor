package com.crypt.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

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
		return new FlatFileItemReaderBuilder<String>()
				.name("cryptLineReader")
				.lineMapper(new PassThroughLineMapper())
				.resource(new ClassPathResource("sample.txt"))
				.build();
	}

	@Bean
	public Job job() {
		return jobBuilderFactory
				.get("cryptorJob")
				.incrementer(new RunIdIncrementer())
				.start(encryptorStep())
				.build();
	}

	@Bean
	public Step encryptorStep() {
		return stepBuilderFactory.get("step1")
				.<String, String>chunk(2)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();
	}

	@Bean
	public FlatFileItemWriter<String> writer() {
		String fileName = "result/output-" + new SimpleDateFormat("yyyyMMddHHmmss'.txt'").format(new Date());
		
		return new FlatFileItemWriterBuilder<String>()
				.name("cryptLineWriter")
				.lineAggregator(new PassThroughLineAggregator<String>())
				.resource(new FileSystemResource(fileName))
				.build();
	}

	@Bean
	public LineEncryptor processor() {
		return new LineEncryptor();
	}
}
