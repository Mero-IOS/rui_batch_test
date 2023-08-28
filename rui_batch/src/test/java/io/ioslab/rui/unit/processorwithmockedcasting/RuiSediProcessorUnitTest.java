package io.ioslab.rui.unit.processorwithmockedcasting;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import io.ioslab.rui.batch.processor.RuiSediProcessor;
import io.ioslab.rui.batch.utility.Casting;
import io.ioslab.rui.common.model.rui.RuiSedi;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class RuiSediProcessorUnitTest {

    RuiSediProcessor ruiSediProcessor;

    @Test
    void process_anyDate_doesAdd() {
        ruiSediProcessor = RuiSediProcessor.builder().build();
        try (MockedStatic<Casting> castingMockedStatic = mockStatic(Casting.class)) {
            Date testDate = new Date();
            when(Casting.castStringToDate(any())).thenReturn(testDate);
            RuiSedi sedi = new RuiSedi();
            ruiSediProcessor.process(sedi);
            assertThat(sedi.getDataElaborazione()).isEqualTo(testDate);
        }
    }

    @Test
    void process_null_doesKeepNullAsDataElaborazione() {
        ruiSediProcessor = RuiSediProcessor.builder().build();
        try (MockedStatic<Casting> castingMockedStatic = mockStatic(Casting.class)) {
            when(Casting.castStringToDate(any())).thenReturn(null);
            RuiSedi sedi = new RuiSedi();
            ruiSediProcessor.process(sedi);
            assertThat(sedi.getDataElaborazione()).isNull();
        }
    }

    @Test
    void process_anyException_doesRethrowException() {
        ruiSediProcessor = RuiSediProcessor.builder().build();
        try (MockedStatic<Casting> castingMockedStatic = mockStatic(Casting.class)) {
            when(Casting.castStringToDate(any())).thenThrow(new RuntimeException("TEST_EXCEPTION"));
            RuiSedi sedi = new RuiSedi();
            assertThrows(RuntimeException.class, () -> ruiSediProcessor.process(sedi));
        }
    }

}
