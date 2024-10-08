package com.example.Invoice_Generator.uploadfile.config;


import com.example.Invoice_Generator.domain.InvoiceDetails;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public Job importTransportDetailsJob(JobRepository jobRepository, Step step1, JobCompletionNotificationListener listener) {
        return new JobBuilder("importTransportDetailsJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                      TransportDetailsItemReader reader, TransportDetailsItemProcessor processor,
                      TransportDetailsItemWriter writer) {
        return new StepBuilder("step1", jobRepository)
                .<InvoiceDetails, InvoiceDetails>chunk(20000, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    @StepScope
    public TransportDetailsItemReader reader(@Value("#{jobParameters['filePath']}") String filePath) {
        TransportDetailsItemReader reader = new TransportDetailsItemReader();
        reader.setFilePath(filePath);  // Set the file path dynamically
        return reader;
    }

}