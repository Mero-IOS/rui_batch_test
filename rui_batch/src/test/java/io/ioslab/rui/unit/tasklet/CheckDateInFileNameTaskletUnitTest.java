package io.ioslab.rui.unit.tasklet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

import io.ioslab.rui.batch.exception.MailErrorException;
import io.ioslab.rui.batch.tasklet.CheckDateInFileNameTasklet;
import io.ioslab.rui.batch.utility.SendEmailError;
import io.ioslab.rui.utils.TestConstants;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class CheckDateInFileNameTaskletUnitTest {

    CheckDateInFileNameTasklet tasklet;
    private final Resource mockCsvWithValidAndInvalidRecords = new ClassPathResource(
        "mockCsv/mockCsvWithValidAndInvalidRecords");
    private final Resource mockCsvWithSingleWrongDate = new ClassPathResource(
        "mockCsv/mockCsvWithSingleWrongDate");

    @BeforeEach
    void setTasklet() throws IOException {
        tasklet = CheckDateInFileNameTasklet.builder()
                                            .outputPath(mockCsvWithValidAndInvalidRecords.getFile()
                                                                                         .getPath())
                                            .date(TestConstants.CSV_FILE_NAME_DATE)
                                            .build();
    }

    @Test
    void execute_withAllFilesContainingDate_doesNotThrowMailErrorException() throws Exception {
        RepeatStatus shouldBeFinished = tasklet.execute(null, null);
        assertThat(shouldBeFinished).isEqualTo(RepeatStatus.FINISHED);
    }

    @Test
    void execute_withEmptyFolder_doesThrowMailErrorException() throws Exception {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            tasklet = CheckDateInFileNameTasklet.builder()
                                                .outputPath(Files.createTempDirectory("TEST")
                                                                 .toAbsolutePath()
                                                                 .toString())
                                                .date(TestConstants.CSV_FILE_NAME_DATE)
                                                .build();
            assertThrows(MailErrorException.class, () -> tasklet.execute(null, null));
        }
    }

    @Test
    void execute_withNoFilesContainingDate_doesThrowMailErrorException() throws Exception {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            tasklet = CheckDateInFileNameTasklet.builder()
                                                .outputPath(
                                                    mockCsvWithSingleWrongDate.getFile().getPath())
                                                .date(TestConstants.CSV_FILE_NAME_DATE)
                                                .build();
            assertThrows(MailErrorException.class, () -> tasklet.execute(null, null));
        }
    }

}
