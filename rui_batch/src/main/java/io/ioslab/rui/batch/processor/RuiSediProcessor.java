package io.ioslab.rui.batch.processor;

import io.ioslab.rui.common.model.rui.RuiSedi;
import lombok.Builder;
import org.springframework.batch.item.ItemProcessor;

import static io.ioslab.rui.batch.utility.Casting.castStringToDate;

@Builder
public class RuiSediProcessor implements ItemProcessor<RuiSedi, RuiSedi> {

    private String date;

    @Override
    public RuiSedi process(RuiSedi ruiSedi) {
        ruiSedi.setDataElaborazione(castStringToDate(date));
        return ruiSedi;
    }
}
