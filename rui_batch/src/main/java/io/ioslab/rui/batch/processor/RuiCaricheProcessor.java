package io.ioslab.rui.batch.processor;

import io.ioslab.rui.common.model.rui.RuiCariche;
import lombok.Builder;
import org.springframework.batch.item.ItemProcessor;

import static io.ioslab.rui.batch.utility.Casting.castStringToDate;

@Builder
public class RuiCaricheProcessor implements ItemProcessor<RuiCariche, RuiCariche> {

    private String date;

    @Override
    public RuiCariche process(RuiCariche ruiCariche) {
        ruiCariche.setDataElaborazione(castStringToDate(date));
        return ruiCariche;
    }

}
