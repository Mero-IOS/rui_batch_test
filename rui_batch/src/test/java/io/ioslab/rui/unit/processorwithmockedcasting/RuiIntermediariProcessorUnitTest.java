package io.ioslab.rui.unit.processorwithmockedcasting;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
            RuiIntermediari collaboratori = new RuiIntermediari();
            ruiIntermediariProcessor.process(collaboratori);
            assertThat(collaboratori.getDataElaborazione()).isEqualTo(testDate);
        }
    }

}

