package io.ioslab.rui.integration.processorwithcasting;

import static io.ioslab.rui.utils.TestConstants.CSV_DATE_AS_VALID_JOB_PARAMETER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

import io.ioslab.rui.batch.configuration.RuiProcessorConfiguration;
import io.ioslab.rui.batch.exception.MailErrorException;
import io.ioslab.rui.batch.processor.RuiMandatiProcessor;
import io.ioslab.rui.batch.utility.SendEmailError;
import io.ioslab.rui.common.model.rui.RuiMandati;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class RuiMandatiProcessorIntegrationTest {

    private RuiMandatiProcessor ruiMandatiProcessor;

    @Test
    void process_anyDate_doesSetDate() {
        ruiMandatiProcessor = new RuiProcessorConfiguration().processorRuiMandati(
            CSV_DATE_AS_VALID_JOB_PARAMETER);
        RuiMandati mandati = new RuiMandati();
        ruiMandatiProcessor.process(mandati);
        assertThat(mandati.getDataElaborazione()).isEqualTo(CSV_DATE_AS_VALID_JOB_PARAMETER);
    }

    @Test
    void process_nullDate_doesSetDateAsNull() {
        ruiMandatiProcessor = new RuiProcessorConfiguration().processorRuiMandati(null);
        RuiMandati mandati = new RuiMandati();
        ruiMandatiProcessor.process(mandati);
        assertThat(mandati.getDataElaborazione()).isNull();
    }

    @Test
    void process_emptyDate_doesSetDateAsNull() {
        ruiMandatiProcessor = new RuiProcessorConfiguration().processorRuiMandati("");
        RuiMandati mandati = new RuiMandati();
        ruiMandatiProcessor.process(mandati);
        assertThat(mandati.getDataElaborazione()).isNull();
    }

    @Test
    void process_invalidDate_doesThrowMailErrorException() {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            ruiMandatiProcessor = new RuiProcessorConfiguration().processorRuiMandati(
                "INVALID_DATE");
            RuiMandati mandati = new RuiMandati();
            assertThrows(MailErrorException.class, () -> ruiMandatiProcessor.process(mandati));
        }
    }

}
