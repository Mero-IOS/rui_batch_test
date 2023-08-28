package io.ioslab.rui.unit.policies;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

import io.ioslab.rui.batch.exception.MailErrorException;
import io.ioslab.rui.batch.policies.CustomSkipPolicy;
import io.ioslab.rui.batch.utility.SendEmailError;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class CustomSkipPolicyUnitTest {

    @Test
    void shouldSkip_overTen_doesThrowException() {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            CustomSkipPolicy customSkipPolicy = CustomSkipPolicy.builder().build();
            RuntimeException testException = new RuntimeException("TEST_EXCEPTION");
            assertThrows(MailErrorException.class, () -> customSkipPolicy.shouldSkip(testException, 12));
        }
    }

    @Test
    void shouldSkip_belowTen_doesReturnTrue() {
        CustomSkipPolicy customSkipPolicy = CustomSkipPolicy.builder().build();
        assertTrue(customSkipPolicy.shouldSkip(new RuntimeException("TEST_EXCEPTION"), 9));
    }

}

