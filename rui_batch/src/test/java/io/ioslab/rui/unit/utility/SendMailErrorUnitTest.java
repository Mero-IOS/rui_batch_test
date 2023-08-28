package io.ioslab.rui.unit.utility;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import io.ioslab.rui.batch.utility.SendEmailError;
import io.ioslab.rui.common.service.ini.IniReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.mail.MailException;

class SendMailErrorUnitTest {

    private ByteArrayOutputStream outputStreamTest;
    private MailException exceptionForTest;

    @BeforeEach
    void setOutputStreamTest() {
        outputStreamTest = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outputStreamTest));
        exceptionForTest = mock(MailException.class);
        doAnswer(invocation -> {
            System.err.println("TEST_MAILSEND_EXCEPTION");
            return null;
        }).when(exceptionForTest).printStackTrace();
    }

    @Test
    void manageError_anyString_doesSendMail() {
        try (MockedStatic<IniReader> iniReaderMockedStatic = mockStatic(IniReader.class)) {
            when(IniReader.getIniReaderder()).thenThrow(exceptionForTest);
            SendEmailError.manageError("ERROR_MESSAGE");
            iniReaderMockedStatic.verify(IniReader::getIniReaderder);
            assertThat(outputStreamTest.toString()).contains("TEST_MAILSEND_EXCEPTION");
        }
    }

    @Test
    void manageError_nullString_doesSendMail() {
        try (MockedStatic<IniReader> iniReaderMockedStatic = mockStatic(IniReader.class)) {
            when(IniReader.getIniReaderder()).thenThrow(exceptionForTest);
            SendEmailError.manageError(null);
            iniReaderMockedStatic.verify(IniReader::getIniReaderder);
            assertThat(outputStreamTest.toString()).contains("TEST_MAILSEND_EXCEPTION");
        }
    }

    @Test
    void manageError_anyStringAndStackTrace_doesSendMail() {
        try (MockedStatic<IniReader> iniReaderMockedStatic = mockStatic(IniReader.class)) {
            when(IniReader.getIniReaderder()).thenThrow(exceptionForTest);
            SendEmailError.manageError("ERROR_MESSAGE", "STACK_TRACE");
            iniReaderMockedStatic.verify(IniReader::getIniReaderder);
            assertThat(outputStreamTest.toString()).contains("TEST_MAILSEND_EXCEPTION");
        }
    }

    @Test
    void manageError_nullStringAndAnyStackTrace_doesSendMail() {
        try (MockedStatic<IniReader> iniReaderMockedStatic = mockStatic(IniReader.class)) {
            when(IniReader.getIniReaderder()).thenThrow(exceptionForTest);
            SendEmailError.manageError(null, "STACK_TRACE");
            iniReaderMockedStatic.verify(IniReader::getIniReaderder);
            assertThat(outputStreamTest.toString()).contains("TEST_MAILSEND_EXCEPTION");
        }
    }

    @Test
    void manageError_anyStringAndNullStackTrace_doesSendMail() {
        try (MockedStatic<IniReader> iniReaderMockedStatic = mockStatic(IniReader.class)) {
            when(IniReader.getIniReaderder()).thenThrow(exceptionForTest);
            SendEmailError.manageError("ERROR_MESSAGE", null);
            iniReaderMockedStatic.verify(IniReader::getIniReaderder);
            assertThat(outputStreamTest.toString()).contains("TEST_MAILSEND_EXCEPTION");
        }
    }

}
