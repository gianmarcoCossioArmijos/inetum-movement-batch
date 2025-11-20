package com.inetum.movement_batch.reader;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.PassThroughFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.core.io.FileSystemResource;

@ExtendWith(MockitoExtension.class)
public class MovementReaderTest {

    @Test
    public void MovementReader_succesfulCsvReading() throws Exception {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(new String[]{
                "operationAmount",
                "operationType",
                "operationDescription",
                "operationCurrency",
                "origin",
                "channel",
                "creditLineId"});

        DefaultLineMapper<FieldSet> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new PassThroughFieldSetMapper());

        FlatFileItemReader<FieldSet> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(0);
        reader.setLineMapper(lineMapper);
        reader.setResource(new FileSystemResource("src/test/java/com/inetum/movement_batch/resources/process-data.csv"));
        reader.open(MetaDataInstanceFactory.createStepExecution().getExecutionContext());

        reader.open(MetaDataInstanceFactory.createStepExecution().getExecutionContext());
        try {
            int count = 0;
            FieldSet line;
            while ((line = reader.read()) != null) {
                Assertions.assertThat("operationAmount").isEqualTo(line.getNames()[0]);
                Assertions.assertThat("operationType").isEqualTo(line.getNames()[1]);
                Assertions.assertThat("operationDescription").isEqualTo(line.getNames()[2]);
                Assertions.assertThat("operationCurrency").isEqualTo(line.getNames()[3]);
                Assertions.assertThat("origin").isEqualTo(line.getNames()[4]);
                Assertions.assertThat("channel").isEqualTo(line.getNames()[5]);
                Assertions.assertThat("creditLineId").isEqualTo(line.getNames()[6]);
                count++;
            }
            Assertions.assertThat(3).isEqualTo(count);
        } catch (Exception e) {
            throw e;
        } finally {
            reader.close();
        }
    }
}
