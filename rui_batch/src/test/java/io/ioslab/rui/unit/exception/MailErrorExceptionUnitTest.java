package io.ioslab.rui.unit.exception;

import io.ioslab.rui.batch.exception.MailErrorException;
import io.ioslab.rui.batch.utility.SendEmailError;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class MailErrorExceptionUnitTest {

    @Test
    void newException_constructorWithOneString_doesSendMail() {
        try (MockedStatic<SendEmailError> sendMailErrorMock = Mockito.mockStatic(
            SendEmailError.class)) {
            new MailErrorException("TEST_EXCEPTION");
            sendMailErrorMock.verify(() -> SendEmailError.manageError("TEST_EXCEPTION"));
        }
    }

    @Test
    void newException_constructorWithTwoStrings_doesSendMail(){
        try (MockedStatic<SendEmailError> sendMailErrorMock = Mockito.mockStatic(
            SendEmailError.class)) {
            new MailErrorException("TEST_EXCEPTION", "TEST_TRACE");
            sendMailErrorMock.verify(
                () -> SendEmailError.manageError("TEST_EXCEPTION", "TEST_TRACE"));
        }
    }

}
