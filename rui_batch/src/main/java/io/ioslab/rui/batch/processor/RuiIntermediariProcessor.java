package io.ioslab.rui.batch.processor;

import io.ioslab.rui.common.model.rui.RuiIntermediari;
import lombok.Builder;
import org.springframework.batch.item.ItemProcessor;

import static io.ioslab.rui.batch.utility.Casting.castStringToDate;

@Builder
public class RuiIntermediariProcessor implements ItemProcessor<RuiIntermediari, RuiIntermediari> {

    private String date;

    @Override
    public RuiIntermediari process(RuiIntermediari ruiIntermediari) {
        ruiIntermediari.setDataElaborazione(castStringToDate(date));
        return ruiIntermediari;
    }
}
