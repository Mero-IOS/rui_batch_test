package io.ioslab.rui.batch.reader;

import io.ioslab.rui.batch.policies.BlankLineRecordSeparatorPolicy;
import io.ioslab.rui.batch.reader.mapper.CustomLineMapper;
import io.ioslab.rui.batch.reader.mapper.RuiIntermediariFieldSetMapper;
import io.ioslab.rui.common.model.rui.RuiIntermediari;
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
public class RuiIntermediariReader {

    private static final String[] NAMES = new String[]{
            "oss",
            "inoperativo",
            "dataInizioInoperativita",
            "numeroIscrizioneRui",
            "dataIscrizione",
            "cognomeNome",
            "stato",
            "comuneNascita",
            "dataNascita",
            "ragioneSociale",
            "provinciaNascita",
            "titoloIndividialeSezioneA",
            "attivitaEsericitataSezioneA",
            "titoloIndividialeSezioneB",
            "attivitaEsericitataSezioneB"
    };

    @Bean
    @StepScope
    public FlatFileItemReader<RuiIntermediari> readerRuiIntermediari(
            @Value("#{jobExecutionContext['" + PARAMETER_OUTPUT_PATH + "']}") String outputPath,
            @Value("#{jobExecutionContext['" + PARAMETER_DATE_CSV + "']}") String date) {
        return new FlatFileItemReaderBuilder<RuiIntermediari>()
                .name("readerRuiIntermediari")
                .resource(new FileSystemResource(createFileName(outputPath, FILE_NAME_RUI_INTERMEDIARI, date)))
                .recordSeparatorPolicy(new BlankLineRecordSeparatorPolicy())
                .lineMapper(new CustomLineMapper<>(RuiIntermediari.class,
                        new RuiIntermediariFieldSetMapper()).mapper(NAMES))
                .linesToSkip(1)
                .build();
    }
}
