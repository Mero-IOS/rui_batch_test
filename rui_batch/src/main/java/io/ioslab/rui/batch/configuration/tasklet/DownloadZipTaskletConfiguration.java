package io.ioslab.rui.batch.configuration.tasklet;

import io.ioslab.rui.batch.tasklet.DownLoadZipTasklet;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.ioslab.rui.batch.utility.Costants.*;

@Configuration
class DownloadZipTaskletConfiguration {

    @Bean
    @StepScope
    public Tasklet downloadZipTasklet(@Value("#{jobExecutionContext['" + PARAMETER_URL + "']}") String url,
                                      @Value("#{jobExecutionContext['" + PARAMETER_DATE_CSV + "']}") String date,
                                      @Value("#{jobExecutionContext['" + PARAMETER_OUTPUT_PATH + "']}") String outputPath) {
        return DownLoadZipTasklet.builder()
                .url(url)
                .date(date)
                .outputPath(outputPath)
                .build();
    }
}
