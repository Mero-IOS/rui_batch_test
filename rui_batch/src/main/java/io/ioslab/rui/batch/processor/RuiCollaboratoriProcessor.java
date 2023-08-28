package io.ioslab.rui.batch.processor;

import io.ioslab.rui.common.model.rui.RuiCollaboratori;
import lombok.Builder;
import org.springframework.batch.item.ItemProcessor;

import static io.ioslab.rui.batch.utility.Casting.castStringToDate;

@Builder
public class RuiCollaboratoriProcessor implements ItemProcessor<RuiCollaboratori, RuiCollaboratori> {

    private String date;

    @Override
    public RuiCollaboratori process(RuiCollaboratori ruiCollaboratori) {
        ruiCollaboratori.setDataElaborazione(castStringToDate(date));
        return ruiCollaboratori;
    }
}
