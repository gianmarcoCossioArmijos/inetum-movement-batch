package com.inetum.movement_batch.writer;

import com.inetum.movement_batch.dto.RestMovementResponse;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

public class MovementWriter {

//    @Bean
//    public FlatFileItemWriter write(Chunk<? extends RestMovementResponse> chunk) throws Exception {
//        System.out.println("Writing movement parquet...");
//        LocalDateTime date = LocalDateTime.now();
//        String filename = String.format("C:/Users/gianmarco.armijos/Downloads/movement_parquet_%s.csv");
//
//        // configure output resurce file
//        Resource resource = new FileSystemResource(filename);
//        FlatFileItemWriter<RestMovementResponse> writer = new FlatFileItemWriter<>();
//        writer.setResource((WritableResource) resource);
//        writer.setAppendAllowed(true);
//
//        for (RestMovementResponse movement : chunk.getItems()) {
//
//            // Configure the line aggregator and csv delimiter
//            DelimitedLineAggregator<RestMovementResponse> lineAggregator = new DelimitedLineAggregator<>();
//            lineAggregator.setDelimiter(",");
//
//            // configure the field extractor to get data
//            BeanWrapperFieldExtractor<RestMovementResponse> fieldExtractor = new BeanWrapperFieldExtractor<>();
//            fieldExtractor.setNames(new String[] {"responseCreditAmount", "responseCurrentAmount", "response", "responseMessage"});
//            lineAggregator.setFieldExtractor(fieldExtractor);
//
//            writer.setLineAggregator(lineAggregator);
//            return writer;
//        }
//    }
}
