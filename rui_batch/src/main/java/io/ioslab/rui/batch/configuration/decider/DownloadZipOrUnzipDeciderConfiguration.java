package io.ioslab.rui.batch.configuration.decider;

import io.ioslab.rui.batch.decider.DownloadZipOrUnzipDecider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DownloadZipOrUnzipDeciderConfiguration {

    @Bean
    public DownloadZipOrUnzipDecider deciderDownloadZipOrUnzip() {
        return new DownloadZipOrUnzipDecider();
    }
}
