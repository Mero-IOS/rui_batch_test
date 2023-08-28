package io.ioslab.rui.integration.processorwithcasting;

import static io.ioslab.rui.utils.TestConstants.CSV_DATE_AS_VALID_JOB_PARAMETER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

import io.ioslab.rui.batch.configuration.RuiProcessorConfiguration;
import io.ioslab.rui.batch.exception.MailErrorException;
import io.ioslab.rui.batch.processor.RuiSediProcessor;
import io.ioslab.rui.batch.utility.SendEmailError;
import io.ioslab.rui.common.model.rui.RuiSedi;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class RuiSediProcessorUnitTest {

    private RuiSediProcessor ruiSediProcessor;

    @Test
    void process_anyDate_doesSetDate() {
        ruiSediProcessor = new RuiProcessorConfiguration().processorRuiSedi(
            CSV_DATE_AS_VALID_JOB_PARAMETER);
        RuiSedi sedi = new RuiSedi();
        ruiSediProcessor.process(sedi);
        assertThat(sedi.getDataElaborazione()).isEqualTo(CSV_DATE_AS_VALID_JOB_PARAMETER);
    }

    @Test
    void process_nullDate_doesSetDateAsNull() {
        ruiSediProcessor = new RuiProcessorConfiguration().processorRuiSedi(null);
        RuiSedi sedi = new RuiSedi();
        ruiSediProcessor.process(sedi);
        assertThat(sedi.getDataElaborazione()).isNull();
    }

    @Test
    void process_emptyDate_doesSetDateAsNull() {
        ruiSediProcessor = new RuiProcessorConfiguration().processorRuiSedi("");
        RuiSedi sedi = new RuiSedi();
        ruiSediProcessor.process(sedi);
        assertThat(sedi.getDataElaborazione()).isNull();
    }

    @Test
    void process_invalidDate_doesThrowMailErrorException() {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            ruiSediProcessor = new RuiProcessorConfiguration().processorRuiSedi("INVALID_DATE");
            RuiSedi sedi = new RuiSedi();
            assertThrows(MailErrorException.class, () -> ruiSediProcessor.process(sedi));
        }
    }

}
