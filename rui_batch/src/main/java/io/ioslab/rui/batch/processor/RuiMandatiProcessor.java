package io.ioslab.rui.batch.processor;

import io.ioslab.rui.common.model.rui.RuiMandati;
import lombok.Builder;
import org.springframework.batch.item.ItemProcessor;

import static io.ioslab.rui.batch.utility.Casting.castStringToDate;

@Builder
public class RuiMandatiProcessor implements ItemProcessor<RuiMandati, RuiMandati> {

    private String date;

    @Override
    public RuiMandati process(RuiMandati ruiMandati) {
        ruiMandati.setDataElaborazione(castStringToDate(date));
        return ruiMandati;
    }
}
