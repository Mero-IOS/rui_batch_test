package io.ioslab.rui.unit.decider;

import static io.ioslab.rui.batch.utility.Costants.FLOW_DECIDER_DOWNLOAD;
import static io.ioslab.rui.batch.utility.Costants.FLOW_DECIDER_UNZIP;
import static io.ioslab.rui.batch.utility.Costants.PARAMETER_URL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.ioslab.rui.batch.decider.DownloadZipOrUnzipDecider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.item.ExecutionContext;

class DownloadZipOrUnzipDeciderUnitTest {

    DownloadZipOrUnzipDecider toTest;
    JobExecution jobExecutionMock;
    ExecutionContext executionContextMock;

    @BeforeEach
    void setup() {
        jobExecutionMock = mock(JobExecution.class);
        executionContextMock = mock(ExecutionContext.class);
        toTest = new DownloadZipOrUnzipDecider();
    }

    @Test
    void decide_withUrl_doesReturnFlowDeciderDownload() {
        when(jobExecutionMock.getExecutionContext()).thenReturn(executionContextMock);
        when(executionContextMock.get(PARAMETER_URL)).thenReturn("ANY_STRING");
        assertThat(toTest.decide(jobExecutionMock, mock(StepExecution.class))).isEqualTo(
            new FlowExecutionStatus(FLOW_DECIDER_DOWNLOAD));
    }

    @Test
    void decide_withoutUrl_doesReturnFlowDeciderUnzip() {
        when(jobExecutionMock.getExecutionContext()).thenReturn(executionContextMock);
        when(executionContextMock.get(PARAMETER_URL)).thenReturn(null);
        assertThat(toTest.decide(jobExecutionMock, mock(StepExecution.class))).isEqualTo(
            new FlowExecutionStatus(FLOW_DECIDER_UNZIP));
    }

    @Test
    void decide_withEmptyUrl_doesReturnFlowDeciderUnzip() {
        when(jobExecutionMock.getExecutionContext()).thenReturn(executionContextMock);
        when(executionContextMock.get(PARAMETER_URL)).thenReturn("");
        assertThat(toTest.decide(jobExecutionMock, mock(StepExecution.class))).isEqualTo(
            new FlowExecutionStatus(FLOW_DECIDER_UNZIP));
    }

}
