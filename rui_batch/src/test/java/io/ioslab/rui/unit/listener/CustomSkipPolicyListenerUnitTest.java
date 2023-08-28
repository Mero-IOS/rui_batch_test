package io.ioslab.rui.unit.listener;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import io.ioslab.rui.batch.configuration.policies.CustomSkipPolicyListenerConfiguration;
import io.ioslab.rui.batch.exception.MailErrorException;
import io.ioslab.rui.batch.listener.CustomSkipPolicyListener;
import io.ioslab.rui.batch.utility.SendEmailError;
import io.ioslab.rui.common.model.rui.Rui;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class CustomSkipPolicyListenerUnitTest {

    CustomSkipPolicyListener customSkipPolicyListener;
    Path tempPath;

    @BeforeEach
    void setCustomSkipPolicyListener() throws IOException {
        tempPath = Files.createTempDirectory("RuiTest");
        customSkipPolicyListener = new CustomSkipPolicyListenerConfiguration().listenerCustomSkipPolicy(tempPath.toString());
    }

    @Test
    void onSkipInRead_anyException_doesCreateWasteFile() {
        customSkipPolicyListener.onSkipInRead(new Exception("TEST_EXCEPTION"));
        assertThat(tempPath.resolve("scarti.txt").toFile()).exists();
    }

    @Test
    void onSkipInRead_anyException_doesAddExceptionLogToWasteFile() throws IOException {
        customSkipPolicyListener.onSkipInRead(new Exception("TEST_EXCEPTION"));
        String scartiFileContents = new String(Files.readAllBytes(tempPath.resolve("scarti.txt")));
        assertThat(scartiFileContents).contains("reader - \n");
    }

    @Test
    void onSkipInWrite_anyExceptionAnyItem_doesAddExceptionLogToWasteFile() throws IOException {
        customSkipPolicyListener.onSkipInWrite(new Rui(), new Exception("TEST_EXCEPTION"));
        String scartiFileContents = new String(Files.readAllBytes(tempPath.resolve("scarti.txt")));
        assertThat(scartiFileContents).contains("writer - io.ioslab.rui.common.model.rui.Rui");
    }

    @Test
    void onSkipInWrite_anyExceptionNull_doesAddExceptionLogToWasteFile() throws IOException {
        customSkipPolicyListener.onSkipInWrite(null, new Exception("TEST_EXCEPTION"));
        String scartiFileContents = new String(Files.readAllBytes(tempPath.resolve("scarti.txt")));
        assertThat(scartiFileContents).contains("writer - \n");
    }

    @Test
    void onSkipInProcess_anyExceptionAnyItem_doesAddExceptionLogToWasteFile() throws IOException {
        customSkipPolicyListener.onSkipInProcess(new Rui(), new Exception("TEST_EXCEPTION"));
        String scartiFileContents = new String(Files.readAllBytes(tempPath.resolve("scarti.txt")));
        assertThat(scartiFileContents).contains("process - io.ioslab.rui.common.model.rui.Rui");
    }

    @Test
    void onSkipInProcess_anyExceptionNullItem_doesAddExceptionLogToWasteFile() throws IOException {
        customSkipPolicyListener.onSkipInProcess(null, new Exception("TEST_EXCEPTION"));
        String scartiFileContents = new String(Files.readAllBytes(tempPath.resolve("scarti.txt")));
        assertThat(scartiFileContents).contains("process - \n");
    }

    @Test
    void constructor_notADirectoryOutput_doesThrowMailErrorException() {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            String invalidPathString = tempPath.resolve("file.txt").toString();
            assertThrows(MailErrorException.class,
                         () -> new CustomSkipPolicyListener(invalidPathString));
        }
    }

    @Test
    void writeLineInWasteFileWithoutObject_onIOException_doesThrowMailErrorException() {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class);
             MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            when(Files.write(any(Path.class), any(byte[].class), any(OpenOption.class))).thenThrow(
                new IOException());
            RuntimeException testException = new RuntimeException("TEST_EXCEPTION");
            assertThrows(MailErrorException.class,
                         () -> customSkipPolicyListener.onSkipInRead(testException));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void writeLineInWasteFileWithObject_onIOException_doesThrowMailErrorException() {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class);
             MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            when(Files.write(any(Path.class), any(byte[].class), any(OpenOption.class))).thenThrow(
                new IOException());
            RuntimeException testException = new RuntimeException("TEST_EXCEPTION");
            Rui anyItem = new Rui();
            assertThrows(MailErrorException.class,
                         () -> customSkipPolicyListener.onSkipInProcess(anyItem, testException));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
