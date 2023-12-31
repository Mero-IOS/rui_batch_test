package io.ioslab.rui.unit.processorwithmockedcasting;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import io.ioslab.rui.batch.processor.RuiIntermediariProcessor;
import io.ioslab.rui.batch.utility.Casting;
import io.ioslab.rui.common.model.rui.RuiIntermediari;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class RuiIntermediariProcessorUnitTest {

    RuiIntermediariProcessor ruiIntermediariProcessor;

    @Test
    void process_anyDate_doesAdd() {
        ruiIntermediariProcessor = RuiIntermediariProcessor.builder().build();
        try (MockedStatic<Casting> castingMockedStatic = mockStatic(Casting.class)) {
            Date testDate = new Date();
            when(Casting.castStringToDate(any())).thenReturn(testDate);
            RuiIntermediari intermediari = new RuiIntermediari();
            ruiIntermediariProcessor.process(intermediari);
            assertThat(intermediari.getDataElaborazione()).isEqualTo(testDate);
        }
    }
    @Test
    void process_null_doesKeepNullAsDataElaborazione() {
        ruiIntermediariProcessor = RuiIntermediariProcessor.builder().build();
        try (MockedStatic<Casting> castingMockedStatic = mockStatic(Casting.class)) {
            when(Casting.castStringToDate(any())).thenReturn(null);
            RuiIntermediari intermediari = new RuiIntermediari();
            ruiIntermediariProcessor.process(intermediari);
            assertThat(intermediari.getDataElaborazione()).isNull();
        }
    }

    @Test
    void process_anyException_doesRethrowException() {
        ruiIntermediariProcessor = RuiIntermediariProcessor.builder().build();
        try (MockedStatic<Casting> castingMockedStatic = mockStatic(Casting.class)) {
            when(Casting.castStringToDate(any())).thenThrow(new RuntimeException("TEST_EXCEPTION"));
            RuiIntermediari intermediari = new RuiIntermediari();
            assertThrows(RuntimeException.class, () -> ruiIntermediariProcessor.process(intermediari));
        }
    }
}

