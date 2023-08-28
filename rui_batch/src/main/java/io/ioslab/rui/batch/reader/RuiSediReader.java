package io.ioslab.rui.batch.reader;

import io.ioslab.rui.batch.policies.BlankLineRecordSeparatorPolicy;
import io.ioslab.rui.batch.reader.mapper.CustomLineMapper;
import io.ioslab.rui.common.model.rui.RuiSedi;
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
public class RuiSediReader {

    private static final String[] NAMES = new String[]{
            "oss",
            "numeroIscrizioneInt",
            "tipoSede",
            "comuneSede",
            "provinciaSede",
            "capSede",
            "indirizzoSede"
    };

    @Bean
    @StepScope
    public FlatFileItemReader<RuiSedi> readerRuiSedi(
            @Value("#{jobExecutionContext['" + PARAMETER_OUTPUT_PATH + "']}") String outputPath,
            @Value("#{jobExecutionContext['" + PARAMETER_DATE_CSV + "']}") String date) {
        return new FlatFileItemReaderBuilder<RuiSedi>()
                .name("readerRuiSedi")
                .resource(new FileSystemResource(createFileName(outputPath, FILE_NAME_RUI_SEDI, date)))
                .recordSeparatorPolicy(new BlankLineRecordSeparatorPolicy())
                .lineMapper(new CustomLineMapper<>(RuiSedi.class).mapper(NAMES))
                .linesToSkip(1)
                .build();
    }
}
