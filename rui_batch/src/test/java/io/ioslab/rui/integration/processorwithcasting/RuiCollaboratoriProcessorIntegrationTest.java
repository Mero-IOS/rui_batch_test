package io.ioslab.rui.integration.processorwithcasting;

import static io.ioslab.rui.utils.TestConstants.CSV_DATE_AS_VALID_JOB_PARAMETER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

import io.ioslab.rui.batch.configuration.RuiProcessorConfiguration;
import io.ioslab.rui.batch.exception.MailErrorException;
import io.ioslab.rui.batch.processor.RuiCollaboratoriProcessor;
import io.ioslab.rui.batch.utility.SendEmailError;
import io.ioslab.rui.common.model.rui.RuiCollaboratori;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class RuiCollaboratoriProcessorIntegrationTest {

    private RuiCollaboratoriProcessor ruiCollaboratoriProcessor;

    @Test
    void process_anyDate_doesSetDate() {
        ruiCollaboratoriProcessor = new RuiProcessorConfiguration().processorRuiCollaboratori(
            CSV_DATE_AS_VALID_JOB_PARAMETER);
        RuiCollaboratori collaboratori = new RuiCollaboratori();
        ruiCollaboratoriProcessor.process(collaboratori);
        assertThat(collaboratori.getDataElaborazione()).isEqualTo(CSV_DATE_AS_VALID_JOB_PARAMETER);
    }

    @Test
    void process_nullDate_doesSetDateAsNull() {
        ruiCollaboratoriProcessor = new RuiProcessorConfiguration().processorRuiCollaboratori(null);
        RuiCollaboratori collaboratori = new RuiCollaboratori();
        ruiCollaboratoriProcessor.process(collaboratori);
        assertThat(collaboratori.getDataElaborazione()).isNull();
    }

    @Test
    void process_emptyDate_doesSetDateAsNull() {
        ruiCollaboratoriProcessor = new RuiProcessorConfiguration().processorRuiCollaboratori("");
        RuiCollaboratori collaboratori = new RuiCollaboratori();
        ruiCollaboratoriProcessor.process(collaboratori);
        assertThat(collaboratori.getDataElaborazione()).isNull();
    }

    @Test
    void process_invalidDate_doesThrowMailErrorException() {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            ruiCollaboratoriProcessor = new RuiProcessorConfiguration().processorRuiCollaboratori(
                "INVALID_DATE");
            RuiCollaboratori collaboratori = new RuiCollaboratori();
            assertThrows(MailErrorException.class,
                         () -> ruiCollaboratoriProcessor.process(collaboratori));
        }
    }

}
