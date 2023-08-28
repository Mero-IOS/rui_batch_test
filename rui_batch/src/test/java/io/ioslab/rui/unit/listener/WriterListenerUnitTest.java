package io.ioslab.rui.unit.listener;

import static org.mockito.Mockito.mockStatic;

import io.ioslab.rui.batch.listener.ReaderListener;
import io.ioslab.rui.batch.listener.WriterListener;
import io.ioslab.rui.batch.utility.SendEmailError;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class WriterListenerUnitTest {

    WriterListener writerListener;

    @Test
    void onWriteException_doesSendMail() {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            writerListener = WriterListener.builder().fileName("TEST_FILE").build();
            Exception e = new Exception("TEST_EXCEPTION");
            writerListener.onWriteError(e, new ArrayList<>());
            sendEmailErrorMockedStatic.verify(() -> SendEmailError.manageError(
                "errore durante la scrittura del file: TEST_FILE"));
        }
    }

}
