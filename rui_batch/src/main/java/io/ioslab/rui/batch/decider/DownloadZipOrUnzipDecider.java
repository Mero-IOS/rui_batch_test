package io.ioslab.rui.batch.decider;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import java.util.Objects;

import static io.ioslab.rui.batch.utility.Costants.*;

public class DownloadZipOrUnzipDecider implements JobExecutionDecider {

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        boolean urlIsJobParameters = checkUrl(jobExecution
                .getExecutionContext()
                .get(PARAMETER_URL));

        if (!urlIsJobParameters)
            return new FlowExecutionStatus(FLOW_DECIDER_UNZIP);
        else
            return new FlowExecutionStatus(FLOW_DECIDER_DOWNLOAD);
    }

    private boolean checkUrl(Object object) {
        if (Objects.isNull(object))
            return false;
        else
            return !Objects.requireNonNull(object).toString().isEmpty();
    }
}
