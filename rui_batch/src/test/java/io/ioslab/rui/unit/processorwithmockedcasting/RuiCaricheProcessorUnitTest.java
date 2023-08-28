package io.ioslab.rui.unit.processorwithmockedcasting;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import io.ioslab.rui.batch.processor.RuiCaricheProcessor;
import io.ioslab.rui.batch.utility.Casting;
import io.ioslab.rui.common.model.rui.RuiCariche;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class RuiCaricheProcessorUnitTest {

    RuiCaricheProcessor ruiCaricheProcessor;

    @Test
    void process_anyDate_doesAddDateToProcessedItem() {
        ruiCaricheProcessor = RuiCaricheProcessor.builder().build();
        try (MockedStatic<Casting> castingMockedStatic = mockStatic(Casting.class)) {
            Date testDate = new Date();
            when(Casting.castStringToDate(any())).thenReturn(testDate);
            RuiCariche cariche = new RuiCariche();
            ruiCaricheProcessor.process(cariche);
            assertThat(cariche.getDataElaborazione()).isEqualTo(testDate);
        }
    }

    @Test
    void process_null_doesKeepNullAsDataElaborazione() {
        ruiCaricheProcessor = RuiCaricheProcessor.builder().build();
        try (MockedStatic<Casting> castingMockedStatic = mockStatic(Casting.class)) {
            when(Casting.castStringToDate(any())).thenReturn(null);
            RuiCariche cariche = new RuiCariche();
            ruiCaricheProcessor.process(cariche);
            assertThat(cariche.getDataElaborazione()).isNull();
        }
    }

    @Test
    void process_anyException_doesRethrowException() {
        ruiCaricheProcessor = RuiCaricheProcessor.builder().build();
        try (MockedStatic<Casting> castingMockedStatic = mockStatic(Casting.class)) {
            when(Casting.castStringToDate(any())).thenThrow(new RuntimeException("TEST_EXCEPTION"));
            RuiCariche cariche = new RuiCariche();
            assertThrows(RuntimeException.class, () -> ruiCaricheProcessor.process(cariche));
        }
    }

}
