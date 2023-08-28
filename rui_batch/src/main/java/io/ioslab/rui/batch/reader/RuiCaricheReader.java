package io.ioslab.rui.batch.reader;

import io.ioslab.rui.batch.policies.BlankLineRecordSeparatorPolicy;
import io.ioslab.rui.batch.reader.mapper.CustomLineMapper;
import io.ioslab.rui.common.model.rui.RuiCariche;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import static io.ioslab.rui.batch.utility.Costants.*;
import static io.ioslab.rui.batch.utility.CreateFileNameForReader.createFileName;

@Configuration
public class RuiCaricheReader {

    private static final String[] NAMES = new String[]{
            "oss",
            "numeroIscrizioneRuiPf",
            "numeroIscrizioneRuiPg",
            "qualificaIntermediario"
    };

    @Bean
    @StepScope
    public FlatFileItemReader<RuiCariche> readerRuiCariche(@Value("#{jobExecutionContext['" + PARAMETER_OUTPUT_PATH + "']}") String outputPath,
                                                           @Value("#{jobExecutionContext['" + PARAMETER_DATE_CSV + "']}") String date) {
        return new FlatFileItemReaderBuilder<RuiCariche>()
                .name("readerRuiCariche")
                .resource(new FileSystemResource(createFileName(outputPath, FILE_NAME_RUI_CARICHE, date)))
                .recordSeparatorPolicy(new BlankLineRecordSeparatorPolicy())
                .lineMapper(new CustomLineMapper<>(RuiCariche.class).mapper(NAMES))
                .linesToSkip(1)
                .build();
    }
}
