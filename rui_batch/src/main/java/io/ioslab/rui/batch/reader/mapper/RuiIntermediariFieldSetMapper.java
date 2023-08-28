package io.ioslab.rui.batch.reader.mapper;

import io.ioslab.rui.common.model.rui.RuiIntermediari;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import static io.ioslab.rui.batch.utility.Casting.castStringToDate;

public class RuiIntermediariFieldSetMapper implements FieldSetMapper<RuiIntermediari> {

    @Override
    public RuiIntermediari mapFieldSet(FieldSet fieldSet) {
        RuiIntermediari intermediario = new RuiIntermediari();
        intermediario.setId(fieldSet.readLong(0));
        intermediario.setInoperativo(fieldSet.readInt(1));
        intermediario.setDataInizioInoperativita(castStringToDate(fieldSet.readString(2)));
        intermediario.setNumeroIscrizioneRui(fieldSet.readString(3));
        intermediario.setDataIscrizione(castStringToDate(fieldSet.readString(4)));
        intermediario.setCognomeNome(fieldSet.readString(5));
        intermediario.setStato(fieldSet.readString(6));
        intermediario.setComuneNascita(fieldSet.readString(7));
        intermediario.setDataNascita(castStringToDate(fieldSet.readString(8)));
        intermediario.setRagioneSociale(fieldSet.readString(9));
        intermediario.setProvinciaNascita(fieldSet.readString(10));
        intermediario.setTitoloIndividialeSezioneA(fieldSet.readString(11));
        intermediario.setAttivitaEsericitataSezioneA(fieldSet.readString(12));
        intermediario.setTitoloIndividialeSezioneB(fieldSet.readString(13));
        intermediario.setAttivitaEsericitataSezioneB(fieldSet.readString(14));
        return intermediario;
    }
}
