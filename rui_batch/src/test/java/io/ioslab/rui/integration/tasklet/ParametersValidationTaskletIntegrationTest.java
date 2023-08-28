package io.ioslab.rui.integration.tasklet;

import static io.ioslab.rui.batch.utility.Costants.PARAMETER_DATE_CSV;
import static io.ioslab.rui.utils.TestUtils.getExecutionContext;
import static io.ioslab.rui.utils.TestUtils.mockChunkContext;
import static io.ioslab.rui.utils.TestConstants.CSV_DATE_AS_VALID_JOB_PARAMETER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import io.ioslab.rui.batch.exception.MailErrorException;
import io.ioslab.rui.batch.tasklet.ParametersValidationTasklet;
import io.ioslab.rui.batch.utility.SendEmailError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;

class ParametersValidationTaskletIntegrationTest {

    private ParametersValidationTasklet parametersValidationTasklet;
    private ChunkContext chunkContextMock;
    private static final String ANY_PARAM = "SOME_STRING";
    private Path tempOut;

    @BeforeEach
    void setParametersValidationTasklet() throws IOException {
        chunkContextMock = mockChunkContext();
        tempOut = Files.createTempDirectory(ANY_PARAM);
        parametersValidationTasklet = ParametersValidationTasklet.builder()
                                                                 .url(ANY_PARAM)
                                                                 .outputPath(tempOut.toString())
                                                                 .date(ANY_PARAM)
                                                                 .build();
    }

    @Test
    void insertDateInContextIfIsInValid_withEmptyDate_doesThrowMailErrorException() {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            assertThrows(MailErrorException.class,
                         () -> parametersValidationTasklet.execute(mock(StepContribution.class),
                                                                   chunkContextMock));
        }
    }

    @Test
    void insertDateInContextIfIsValid_withDate_doesInsertDateInChunkContext() throws Exception {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            parametersValidationTasklet = ParametersValidationTasklet.builder()
                                                                     .url(ANY_PARAM)
                                                                     .outputPath(tempOut.toString())
                                                                     .date(
                                                                         CSV_DATE_AS_VALID_JOB_PARAMETER)
                                                                     .build();
            parametersValidationTasklet.execute(mock(StepContribution.class), chunkContextMock);
            assertThat(
                getExecutionContext(chunkContextMock).getString(PARAMETER_DATE_CSV)).isEqualTo(
                CSV_DATE_AS_VALID_JOB_PARAMETER);
        }
    }

}