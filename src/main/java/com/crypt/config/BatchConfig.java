package com.crypt.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import com.crypt.listeners.JobAlterListener;
import com.crypt.steps.LineEncryptor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	
	private static final Logger logger = LogManager.getLogger(BatchConfig.class);
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Value("${numberOfThreads}")
    private String numberOfThreads;
	
	@Value("${pathToInputFile}")
    private String pathToInputFile;
	
	@Bean
	public Job job() {
		return jobBuilderFactory
				.get("cryptorJob")
				.listener(new JobAlterListener())
				.incrementer(new RunIdIncrementer())
				.flow(encryptorStep())
				.end()
				.build();
	}

	@Bean
	public Step encryptorStep() {
		return stepBuilderFactory.get("encryptorStep")
				.<String, String>chunk(2)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.taskExecutor(taskExecutor())
				.throttleLimit(Integer.parseInt(this.numberOfThreads))
				.build();
	}

	@Bean
	public TaskExecutor taskExecutor(){
	    SimpleAsyncTaskExecutor asyncTaskExecutor=new SimpleAsyncTaskExecutor("file_encryptor");
	    asyncTaskExecutor.setConcurrencyLimit(Integer.parseInt(this.numberOfThreads));
	    return asyncTaskExecutor;
	}
	
	@Bean
	@StepScope
	public FlatFileItemReader<String> reader() {
		return new FlatFileItemReaderBuilder<String>()
				.name("cryptLineReader")
				.lineMapper(new PassThroughLineMapper())
				.resource(new FileSystemResource(pathToInputFile))
				.build();
	}
	
	@Bean
	public FlatFileItemWriter<String> writer() {
		String fileName = "result/output-" + new SimpleDateFormat("yyyyMMddHHmmss'.txt'").format(new Date());
		logger.info("Output file name {}", fileName);
		
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
