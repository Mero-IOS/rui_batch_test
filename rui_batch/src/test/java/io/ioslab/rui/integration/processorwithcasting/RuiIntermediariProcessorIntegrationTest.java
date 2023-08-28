package io.ioslab.rui.integration.processorwithcasting;

import static io.ioslab.rui.utils.TestConstants.CSV_DATE_AS_VALID_JOB_PARAMETER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

import io.ioslab.rui.batch.configuration.RuiProcessorConfiguration;
import io.ioslab.rui.batch.exception.MailErrorException;
import io.ioslab.rui.batch.processor.RuiIntermediariProcessor;

import io.ioslab.rui.batch.utility.SendEmailError;
import io.ioslab.rui.common.model.rui.RuiIntermediari;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class RuiIntermediariProcessorIntegrationTest {
    private RuiIntermediariProcessor ruiIntermediariProcessor;

    @Test
    void process_anyDate_doesSetDate() {
        ruiIntermediariProcessor = new RuiProcessorConfiguration().processorRuiIntermediari(CSV_DATE_AS_VALID_JOB_PARAMETER);
        RuiIntermediari intermediari = new RuiIntermediari();
        ruiIntermediariProcessor.process(intermediari);
        assertThat(intermediari.getDataElaborazione()).isEqualTo(CSV_DATE_AS_VALID_JOB_PARAMETER);
    }


    @Test
    void process_nullDate_doesSetDateAsNull() {
        ruiIntermediariProcessor = new RuiProcessorConfiguration().processorRuiIntermediari(null);
        RuiIntermediari intermediari = new RuiIntermediari();
        ruiIntermediariProcessor.process(intermediari);
        assertThat(intermediari.getDataElaborazione()).isNull();
    }

    @Test
    void process_emptyDate_doesSetDateAsNull() {
        ruiIntermediariProcessor = new RuiProcessorConfiguration().processorRuiIntermediari("");
        RuiIntermediari intermediari = new RuiIntermediari();
        ruiIntermediariProcessor.process(intermediari);
        assertThat(intermediari.getDataElaborazione()).isNull();
    }

    @Test
    void process_invalidDate_doesThrowMailErrorException() {
        try(MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(SendEmailError.class)){
            ruiIntermediariProcessor = new RuiProcessorConfiguration().processorRuiIntermediari("INVALID_DATE");
            RuiIntermediari intermediari = new RuiIntermediari();
            assertThrows(MailErrorException.class, () -> ruiIntermediariProcessor.process(intermediari));
        }
    }
}

