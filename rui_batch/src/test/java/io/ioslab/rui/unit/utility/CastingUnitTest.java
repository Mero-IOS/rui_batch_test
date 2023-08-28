package io.ioslab.rui.unit.utility;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;

import io.ioslab.rui.batch.exception.MailErrorException;
import io.ioslab.rui.batch.utility.Casting;
import io.ioslab.rui.batch.utility.SendEmailError;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class CastingUnitTest {

    @Test
    void castStringToDate_ParsableDate_doesReturnDate() {
        assertThat(Casting.castStringToDate("2023-07-23")).isInstanceOf(Date.class);
    }

    @Test
    void castStringToDate_WrongFormat_doesThrowMailErrorException() {
        try (MockedStatic<SendEmailError> sendMailErrorMock = Mockito.mockStatic(
            SendEmailError.class)) {
            assertThrows(MailErrorException.class, () -> Casting.castStringToDate("23-07-2023"));
        }
    }

    @Test
    void castStringToDate_null_doesReturnNull() {
        assertThat(Casting.castStringToDate(null)).isNull();
    }

    @Test
    void castStringToDate_emptyString_doesReturnNull() {
        assertThat(Casting.castStringToDate("")).isNull();
    }
}

