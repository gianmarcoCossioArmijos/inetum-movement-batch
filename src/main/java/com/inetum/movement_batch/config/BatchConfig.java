package com.inetum.movement_batch.config;

import com.inetum.movement_batch.dto.RestMovementResponse;
import com.inetum.movement_batch.entity.MovementCsv;
import com.inetum.movement_batch.process.MovementProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;

@Configuration
public class BatchConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public FlatFileItemReader<MovementCsv> reader(){
        return new FlatFileItemReaderBuilder<MovementCsv>()
                .name("reader")
                .resource(new ClassPathResource("process-data.csv"))
                .delimited()
                .names("operationAmount",
                        "operationType",
                        "operationDescription",
                        "operationCurrency",
                        "origin",
                        "channel",
                        "creditLineId")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<MovementCsv>() {{
                    setTargetType(MovementCsv.class);}})
                .build();
    }

    @Bean
    public FlatFileItemWriter<RestMovementResponse> writer() {
        Resource resource = new FileSystemResource("C:/Users/gianmarco.armijos/Downloads/movements.csv");
        FlatFileItemWriter<RestMovementResponse> writer = new FlatFileItemWriter<>();

        writer.setResource((WritableResource) resource);
        writer.setAppendAllowed(true);
        writer.setLineAggregator(new DelimitedLineAggregator<>() {
            {   setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<>() {
                    {
                        setNames(new String[]{
                                "responseCreditAmount",
                                "responseCurrentAmount",
                                "responseMessage",
                                "movementId",
                                "operationAmount",
                                "exchangedAmount",
                                "exchangeRate",
                                "createdAt",
                                "operationtype",
                                "operationtDescription",
                                "operationCurrency",
                                "exchangeCurrency",
                                "origin",
                                "channel",
                                "creditLineId"});}});
            }});
        return writer;
    }

    @Bean
    public Step step(PlatformTransactionManager transactionManager,
                     JobRepository jobRepository, MovementProcessor process) {
        return new StepBuilder("read-csv", jobRepository)
                .<MovementCsv, RestMovementResponse>chunk(1, transactionManager)
                .reader(reader())
                .processor(process)
                .writer(writer())
                .build();
    }

    @Bean
    public Job job(JobRepository jobRepository, Step step) {
        return new JobBuilder("save-movement", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }
}
