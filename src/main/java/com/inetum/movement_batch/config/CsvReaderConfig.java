package com.inetum.movement_batch;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import com.inetum.movement_batch.entity.MovementCsv;

@Configuration
public class CsvReaderConfig {

    @Bean
    public FlatFileItemReader<MovementCsv> reader (){
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
            .targetType(MovementCsv.class)
            .build();
    }
}
