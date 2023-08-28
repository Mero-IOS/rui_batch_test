package io.ioslab.rui.batch.jobrepository;

import io.ioslab.rui.batch.decider.DownloadZipOrUnzipDecider;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.ioslab.rui.batch.utility.Costants.FLOW_DECIDER_DOWNLOAD;
import static io.ioslab.rui.batch.utility.Costants.FLOW_DECIDER_UNZIP;

@Configuration
@EnableBatchProcessing
public class Jobs {

    @Bean
    public Job csvToDatabaseJob(JobBuilderFactory jobBuilderFactory,
                                Step parametersValidationStep,
                                DownloadZipOrUnzipDecider deciderDownloadZipOrUnzip,
                                Step downloadZipStep,
                                Step unzipStep,
                                Step checkDateInFileNameStep,
                                Step deleteDuplicateTupleStep,
                                Step ruiCaricheCsvToDatabaseStep,
                                Step ruiCollaboratoriCsvToDatabaseStep,
                                Step ruiIntermediariCsvToDatabaseStep,
                                Step ruiMandatiCsvToDatabaseStep,
                                Step ruiSediCsvToDatabaseStep,
                                Step createOkFileStep) {
        return jobBuilderFactory
                .get("csvToDatabaseJob")
                .incrementer(new RunIdIncrementer())
                .start(parametersValidationStep)
                .next(deciderDownloadZipOrUnzip)
                .on(FLOW_DECIDER_DOWNLOAD)
                .to(downloadZipStep)
                .next(unzipStep)
                .from(deciderDownloadZipOrUnzip)
                .on(FLOW_DECIDER_UNZIP)
                .to(unzipStep)
                .next(checkDateInFileNameStep)
                .next(deleteDuplicateTupleStep)
                .next(ruiCaricheCsvToDatabaseStep)
                .next(ruiCollaboratoriCsvToDatabaseStep)
                .next(ruiIntermediariCsvToDatabaseStep)
                .next(ruiMandatiCsvToDatabaseStep)
                .next(ruiSediCsvToDatabaseStep)
                .next(createOkFileStep)
                .end()
                .build();
    }
}
