package io.ioslab.rui.unit.listener;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

import io.ioslab.rui.batch.listener.ProcessListener;
import io.ioslab.rui.batch.utility.SendEmailError;
import io.ioslab.rui.common.model.rui.Rui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class ProcessListenerUnitTest {

    ProcessListener processListener;

    @BeforeEach
    void setProcessListener() {
        processListener = ProcessListener.builder().fileName("test.csv").build();
    }

    @Test
    void onProcessError_anyRuiAnyException_doesCallManageError() {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            processListener.onProcessError(new Rui(), new Exception("TEST_EXCEPTION"));
            sendEmailErrorMockedStatic.verify(() -> SendEmailError.manageError(any()), times(1));
        }
    }

}
