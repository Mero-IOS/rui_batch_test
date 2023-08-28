package io.ioslab.rui.unit.processorwithmockedcasting;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import io.ioslab.rui.batch.processor.RuiCollaboratoriProcessor;
import io.ioslab.rui.batch.processor.RuiMandatiProcessor;
import io.ioslab.rui.batch.utility.Casting;
import io.ioslab.rui.common.model.rui.RuiCollaboratori;
import io.ioslab.rui.common.model.rui.RuiMandati;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class RuiMandatiProcessorUnitTest {
    RuiMandatiProcessor ruiMandatiProcessor;

    @Test
    void process_anyDate_doesAdd() {
        ruiMandatiProcessor = RuiMandatiProcessor.builder().build();
        try (MockedStatic<Casting> castingMockedStatic = mockStatic(Casting.class)) {
            Date testDate = new Date();
            when(Casting.castStringToDate(any())).thenReturn(testDate);
            RuiMandati mandati = new RuiMandati();
            ruiMandatiProcessor.process(mandati);

            assertThat(mandati.getDataElaborazione()).isEqualTo(testDate);
        }
    }

    @Test
    void process_null_doesKeepNullAsDataElaborazione() {
        ruiMandatiProcessor = RuiMandatiProcessor.builder().build();
        try (MockedStatic<Casting> castingMockedStatic = mockStatic(Casting.class)) {
            when(Casting.castStringToDate(any())).thenReturn(null);
            RuiMandati collaboratori = new RuiMandati();
            ruiMandatiProcessor.process(collaboratori);
            assertThat(collaboratori.getDataElaborazione()).isNull();
        }
    }

    @Test
    void process_anyException_doesRethrowException() {
        ruiMandatiProcessor = RuiMandatiProcessor.builder().build();
        try (MockedStatic<Casting> castingMockedStatic = mockStatic(Casting.class)) {
            when(Casting.castStringToDate(any())).thenThrow(new RuntimeException("TEST_EXCEPTION"));
            RuiMandati collaboratori = new RuiMandati();
            assertThrows(RuntimeException.class, ()->ruiMandatiProcessor.process(collaboratori));
        }
    }
}
