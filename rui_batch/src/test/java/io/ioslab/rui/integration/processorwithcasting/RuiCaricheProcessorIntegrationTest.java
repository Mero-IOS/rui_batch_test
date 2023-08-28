package io.ioslab.rui.integration.processorwithcasting;

import static io.ioslab.rui.utils.TestConstants.CSV_DATE_AS_VALID_JOB_PARAMETER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

import io.ioslab.rui.batch.configuration.RuiProcessorConfiguration;
import io.ioslab.rui.batch.exception.MailErrorException;
import io.ioslab.rui.batch.processor.RuiCaricheProcessor;
import io.ioslab.rui.batch.utility.SendEmailError;
import io.ioslab.rui.common.model.rui.RuiCariche;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.test.context.jdbc.Sql;

class RuiCaricheProcessorIntegrationTest {

    private RuiCaricheProcessor ruiCaricheProcessor;

    @Test
    void process_anyDate_doesSetDate() {
        ruiCaricheProcessor = new RuiProcessorConfiguration().processorRuiCariche(
            CSV_DATE_AS_VALID_JOB_PARAMETER);
        RuiCariche cariche = new RuiCariche();
        ruiCaricheProcessor.process(cariche);
        assertThat(cariche.getDataElaborazione()).isEqualTo(CSV_DATE_AS_VALID_JOB_PARAMETER);
    }

    @Test
    void process_nullDate_doesSetDateAsNull() {
        ruiCaricheProcessor = new RuiProcessorConfiguration().processorRuiCariche(null);
        RuiCariche cariche = new RuiCariche();
        ruiCaricheProcessor.process(cariche);
        assertThat(cariche.getDataElaborazione()).isNull();
    }

    @Test
    void process_emptyDate_doesSetDateAsNull() {
        ruiCaricheProcessor = new RuiProcessorConfiguration().processorRuiCariche("");
        RuiCariche cariche = new RuiCariche();
        ruiCaricheProcessor.process(cariche);
        assertThat(cariche.getDataElaborazione()).isNull();
    }

    @Test
    void process_invalidDate_doesThrowMailErrorException() {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            ruiCaricheProcessor = new RuiProcessorConfiguration().processorRuiCariche(
                "INVALID_DATE");
            RuiCariche cariche = new RuiCariche();
            assertThrows(MailErrorException.class, () -> ruiCaricheProcessor.process(cariche));
        }
    }

}


