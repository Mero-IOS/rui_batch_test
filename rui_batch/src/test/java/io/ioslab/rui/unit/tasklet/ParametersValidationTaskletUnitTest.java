package io.ioslab.rui.unit.tasklet;

import static io.ioslab.rui.batch.utility.Costants.PARAMETER_DATE_CSV;
import static io.ioslab.rui.batch.utility.Costants.PARAMETER_INPUT_PATH;
import static io.ioslab.rui.batch.utility.Costants.PARAMETER_OUTPUT_PATH;
import static io.ioslab.rui.batch.utility.Costants.PARAMETER_URL;
import static io.ioslab.rui.utils.TestUtils.getExecutionContext;
import static io.ioslab.rui.utils.TestUtils.mockChunkContext;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import io.ioslab.rui.batch.exception.MailErrorException;
import io.ioslab.rui.batch.tasklet.ParametersValidationTasklet;
import io.ioslab.rui.batch.utility.Casting;
import io.ioslab.rui.batch.utility.SendEmailError;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;

class ParametersValidationTaskletUnitTest {

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
    void execute_noUrlOrInput_DoesThrowMailErrorExceptionWithMethod() {
        parametersValidationTasklet = ParametersValidationTasklet.builder().build();
        assertThrows(MailErrorException.class, () -> executeTaskletWithMocks());
    }

    @Test
    void execute_bothUrlAndInput_DoesThrowMailErrorException() {
        parametersValidationTasklet = ParametersValidationTasklet.builder()
                                                                 .url(ANY_PARAM)
                                                                 .inputPath(ANY_PARAM)
                                                                 .build();
        assertThrows(MailErrorException.class, () -> executeTaskletWithMocks());
    }

    @Test
    void execute_nullAsInputPath_DoesThrowMailErrorException() {
        parametersValidationTasklet = ParametersValidationTasklet.builder()
                                                                 .date(ANY_PARAM)
                                                                 .outputPath(tempOut.toString())
                                                                 .inputPath(null)
                                                                 .build();
        assertThrows(MailErrorException.class, () -> executeTaskletWithMocks());
    }

    @Test
    void execute_emptyStringAsOutputPath_DoesThrowMailErrorException() {
        parametersValidationTasklet = ParametersValidationTasklet.builder()
                                                                 .date(ANY_PARAM)
                                                                 .outputPath("")
                                                                 .url(ANY_PARAM)
                                                                 .build();
        assertThrows(MailErrorException.class, () -> executeTaskletWithMocks());
    }

    @Test
    void execute_anyValidInputPath_DoesPutInputPathInContext() throws Exception {
        Path tempDir = Files.createTempFile(ANY_PARAM, ".zip");
        parametersValidationTasklet = ParametersValidationTasklet.builder()
                                                                 .inputPath(tempDir.toString())
                                                                 .outputPath(tempOut.toString())
                                                                 .date(ANY_PARAM)
                                                                 .build();
        assertThat(executeTaskletWithMocks().getString(PARAMETER_INPUT_PATH)).isEqualTo(
            tempDir.toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {PARAMETER_DATE_CSV, PARAMETER_URL})
    void execute_anyValidDate_DoesPutDateAndUrlInContext(String value) throws Exception {
        assertThat(executeTaskletWithMocks().getString(value)).contains(ANY_PARAM);
    }

    @Test
    void execute_emptyStringInputPath_DoesNotPutInputPathInContext() throws Exception {
        Path tempDir = Files.createTempFile(ANY_PARAM, ".zip");
        parametersValidationTasklet = ParametersValidationTasklet.builder()
                                                                 .inputPath("")
                                                                 .outputPath(tempOut.toString())
                                                                 .date(ANY_PARAM)
                                                                 .build();
        assertThat(executeTaskletWithMocks().get(PARAMETER_INPUT_PATH)).isNull();
    }


    @Test
    void execute_anyOutputPathAnyDate_DoesCreateOutputFolder() throws Exception {
        assertThat(new File(executeTaskletWithMocks().getString(PARAMETER_OUTPUT_PATH))).exists();
    }

    private ExecutionContext executeTaskletWithMocks() throws Exception {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class);
             MockedStatic<Casting> castingMockedStatic = mockStatic(Casting.class)) {
            when(Casting.castStringToDate(any())).thenReturn(new Date());
            parametersValidationTasklet.execute(mock(StepContribution.class), chunkContextMock);
            return getExecutionContext(chunkContextMock);
        }
    }

}
