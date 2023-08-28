package io.ioslab.rui.unit.exception;

import io.ioslab.rui.batch.exception.MailErrorException;
import io.ioslab.rui.batch.utility.SendEmailError;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class MailErrorExceptionUnitTest {

    @Test
    void newException_WrongFormat_doesThrowMailErrorException() {
        try (MockedStatic<SendEmailError> sendMailErrorMock = Mockito.mockStatic(
            SendEmailError.class)) {
            new MailErrorException("TEST_EXCEPTION");
            sendMailErrorMock.verify(() -> SendEmailError.manageError("TEST_EXCEPTION"));
        }
    }

    @Test
    void castStringToDate_WrongFormat_doesThrowMailErrorException() {
        try (MockedStatic<SendEmailError> sendMailErrorMock = Mockito.mockStatic(
            SendEmailError.class)) {
            new MailErrorException("TEST_EXCEPTION", "TEST_TRACE");
            sendMailErrorMock.verify(() -> SendEmailError.manageError("TEST_EXCEPTION", "TEST_TRACE"));
        }
    }

}
