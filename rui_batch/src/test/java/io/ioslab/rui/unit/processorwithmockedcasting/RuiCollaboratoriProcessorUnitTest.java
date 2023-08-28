package io.ioslab.rui.unit.processorwithmockedcasting;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import io.ioslab.rui.batch.processor.RuiCollaboratoriProcessor;
import io.ioslab.rui.batch.utility.Casting;
import io.ioslab.rui.common.model.rui.RuiCollaboratori;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class RuiCollaboratoriProcessorUnitTest {

    RuiCollaboratoriProcessor ruiCollaboratoriProcessor;

    @Test
    void process_anyDate_doesAdd() {
        ruiCollaboratoriProcessor = RuiCollaboratoriProcessor.builder().build();
        try (MockedStatic<Casting> castingMockedStatic = mockStatic(Casting.class)) {
            Date testDate = new Date();
            when(Casting.castStringToDate(any())).thenReturn(testDate);
            RuiCollaboratori collaboratori = new RuiCollaboratori();
            ruiCollaboratoriProcessor.process(collaboratori);

            assertThat(collaboratori.getDataElaborazione()).isEqualTo(testDate);
        }
    }

    @Test
    void process_null_doesKeepNullAsDataElaborazione() {
        ruiCollaboratoriProcessor = RuiCollaboratoriProcessor.builder().build();
        try (MockedStatic<Casting> castingMockedStatic = mockStatic(Casting.class)) {
            when(Casting.castStringToDate(any())).thenReturn(null);
            RuiCollaboratori collaboratori = new RuiCollaboratori();
            ruiCollaboratoriProcessor.process(collaboratori);
            assertThat(collaboratori.getDataElaborazione()).isNull();
        }
    }

    @Test
    void process_anyException_doesRethrowException() {
        ruiCollaboratoriProcessor = RuiCollaboratoriProcessor.builder().build();
        try (MockedStatic<Casting> castingMockedStatic = mockStatic(Casting.class)) {
            when(Casting.castStringToDate(any())).thenThrow(new RuntimeException("TEST_EXCEPTION"));
            RuiCollaboratori collaboratori = new RuiCollaboratori();
            assertThrows(RuntimeException.class,
                         () -> ruiCollaboratoriProcessor.process(collaboratori));
        }
    }

}
