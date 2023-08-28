package io.ioslab.rui.unit.listener;

import static org.mockito.Mockito.mockStatic;

import io.ioslab.rui.batch.listener.ReaderListener;
import io.ioslab.rui.batch.utility.SendEmailError;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class ReaderListenerUnitTest {

    public ReaderListener readerListener;

    @Test
    void onReadError_anyException_doesThrowMailErrorException() {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            readerListener = ReaderListener.builder().fileName("TEST_FILE").build();
            Exception e = new Exception("TEST_EXCEPTION");
            readerListener.onReadError(e);
            sendEmailErrorMockedStatic.verify(
                () -> SendEmailError.manageError("errore durante la lettura del file: TEST_FILE"));
        }
    }

}
