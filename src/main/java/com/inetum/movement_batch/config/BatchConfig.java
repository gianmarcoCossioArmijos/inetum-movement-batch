package com.inetum.movement_batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.inetum.movement_batch.process.MovementProcessor;
import com.inetum.movement_batch.writer.MovementWriter;
import com.inetum.movement_batch.entity.MovementCsv;
import com.inetum.movement_batch.dto.RestMovementResponse;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final CsvReaderConfig readerConfig;
    private final MovementProcessor processor;
    private final MovementWriter writer;

    @Bean
    public Step step(StepBuilder stepBuilder) {
        return stepBuilder
                .<MovementCsv, RestMovementResponse>chunk(1)
                .reader(readerConfig.movementCsvReader())
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job job(JobBuilder jobBuilder, Step step) {
        return jobBuilder
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }
}
