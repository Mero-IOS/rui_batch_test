package io.ioslab.rui.unit.reader.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.ioslab.rui.batch.reader.mapper.CustomLineMapper;
import io.ioslab.rui.batch.reader.mapper.RuiIntermediariFieldSetMapper;
import io.ioslab.rui.common.model.rui.Rui;
import io.ioslab.rui.common.model.rui.RuiCariche;
import io.ioslab.rui.common.model.rui.RuiCollaboratori;
import io.ioslab.rui.common.model.rui.RuiIntermediari;
import io.ioslab.rui.common.model.rui.RuiMandati;
import io.ioslab.rui.common.model.rui.RuiSedi;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.validation.BindException;

class CustomLineMapperUnitTest {

    @Test
    void mapperRui_wrongStringArray_doesNotMapLine() {
        CustomLineMapper<Rui> customLineMapper = new CustomLineMapper<>(Rui.class);
        LineMapper<Rui> mapper = customLineMapper.mapper("any", "any");
        assertThrows(BindException.class, () -> mapper.mapLine("A;", 1));
    }

    @Test
    void mapperRui_correctStringArray_doesMapLine() throws Exception {
        CustomLineMapper<Rui> customLineMapper = new CustomLineMapper<>(Rui.class);
        LineMapper<Rui> mapper = customLineMapper.mapper("any");
        assertThat(mapper.mapLine("1", 1)).isInstanceOf(Rui.class);
    }

    @Test
    void mapperRuiCariche_wrongStringArray_doesNotMapLine() {
        CustomLineMapper<RuiCariche> customLineMapper = new CustomLineMapper<>(RuiCariche.class);
        LineMapper<RuiCariche> mapper = customLineMapper.mapper("any", "any");
        assertThrows(BindException.class, () -> mapper.mapLine("A;", 1));
    }

    @Test
    void mapperRuiCariche_correctStringArray_doesMapLine() throws Exception {
        CustomLineMapper<RuiCariche> customLineMapper = new CustomLineMapper<>(RuiCariche.class);
        LineMapper<RuiCariche> mapper = customLineMapper.mapper("oss", "numeroIscrizioneRuiPf",
                                                                "numeroIscrizioneRuiPg",
                                                                "qualificaIntermediario");
        assertThat(mapper.mapLine("1;VALID_ISCR_PF;VALID_ISCR_PG;QUALIFICA", 1)).isInstanceOf(
            RuiCariche.class);
    }

    @Test
    void mapperRuiIntermediari_wrongStringArray_doesNotMapLine() {
        CustomLineMapper<RuiIntermediari> customLineMapper = new CustomLineMapper<>(
            RuiIntermediari.class);
        LineMapper<RuiIntermediari> mapper = customLineMapper.mapper("any", "any");
        assertThrows(BindException.class, () -> mapper.mapLine("A;", 1));
    }

    @Test
    void mapperRuiIntermediari_correctStringArray_doesNotMapLineWithoutFieldSetMapper()
        throws Exception {
        CustomLineMapper<RuiIntermediari> customLineMapper = new CustomLineMapper<>(
            RuiIntermediari.class);
        LineMapper<RuiIntermediari> mapper = customLineMapper.mapper("oss", "inoperativo",
                                                                     "dataInizioInoperativita",
                                                                     "numeroIscrizioneRui",
                                                                     "dataIscrizione",
                                                                     "cognomeNome", "stato",
                                                                     "comuneNascita", "dataNascita",
                                                                     "ragioneSociale",
                                                                     "provinciaNascita",
                                                                     "titoloIndividialeSezioneA",
                                                                     "attivitaEsericitataSezioneA",
                                                                     "titoloIndividialeSezioneB",
                                                                     "attivitaEsericitataSezioneB");
        assertThrows(BindException.class, () -> mapper.mapLine(
            "0;0;;MOCK_RUI_ISCR;2000-01-01;MOCK_NAME;MOCK_COUNTRY;MOCK_CITY;1990-01-01;;MC;.;;.;",
            1));
    }

    @Test
    void mapperRuiIntermediari_correctStringArray_doesMapLineWithFieldSetMapper() throws Exception {
        CustomLineMapper<RuiIntermediari> customLineMapper = new CustomLineMapper<>(
            RuiIntermediari.class, new RuiIntermediariFieldSetMapper());
        LineMapper<RuiIntermediari> mapper = customLineMapper.mapper("oss", "inoperativo",
                                                                     "dataInizioInoperativita",
                                                                     "numeroIscrizioneRui",
                                                                     "dataIscrizione",
                                                                     "cognomeNome", "stato",
                                                                     "comuneNascita", "dataNascita",
                                                                     "ragioneSociale",
                                                                     "provinciaNascita",
                                                                     "titoloIndividialeSezioneA",
                                                                     "attivitaEsericitataSezioneA",
                                                                     "titoloIndividialeSezioneB",
                                                                     "attivitaEsericitataSezioneB");

        assertThat(mapper.mapLine(
            "0;0;;MOCK_RUI_ISCR;2000-01-01;MOCK_NAME;MOCK_COUNTRY;MOCK_CITY;1990-01-01;;MC;.;;.;",
            1)).isInstanceOf(RuiIntermediari.class);
    }

    @Test
    void mapperRuiMandati_wrongStringArray_doesNotMapLine() {
        CustomLineMapper<RuiMandati> customLineMapper = new CustomLineMapper<>(RuiMandati.class);
        LineMapper<RuiMandati> mapper = customLineMapper.mapper("any", "any");
        assertThrows(BindException.class, () -> mapper.mapLine("A;", 1));
    }

    @Test
    void mapperRuiMandati_correctStringArray_doesMapLine() throws Exception {
        CustomLineMapper<RuiMandati> customLineMapper = new CustomLineMapper<>(RuiMandati.class);
        LineMapper<RuiMandati> mapper = customLineMapper.mapper("oss", "numeroIscrizioneRui",
                                                                "codiceCompagnia",
                                                                "ragioneSociale");
        assertThat(mapper.mapLine("0;MOCK_RUI_ISCR;MOCKCO_NUM;MOCK_CO_NAME", 1)).isInstanceOf(
            RuiMandati.class);
    }

    @Test
    void mapperRuiSedi_wrongStringArray_doesNotMapLine() {
        CustomLineMapper<RuiSedi> customLineMapper = new CustomLineMapper<>(RuiSedi.class);
        LineMapper<RuiSedi> mapper = customLineMapper.mapper("any", "any");
        assertThrows(BindException.class, () -> mapper.mapLine("A;", 1));
    }

    @Test
    void mapperRuiSedi_correctStringArray_doesMapLine() throws Exception {
        CustomLineMapper<RuiSedi> customLineMapper = new CustomLineMapper<>(RuiSedi.class);
        LineMapper<RuiSedi> mapper = customLineMapper.mapper("oss", "numeroIscrizioneInt",
                                                             "tipoSede", "comuneSede",
                                                             "provinciaSede", "capSede",
                                                             "indirizzoSede");
        assertThat(mapper.mapLine("0;MOCK_INT;MOCK_TIPO;MOCK_COMUNE;MC;16039;MOCK_ADDRESS",
                                  1)).isInstanceOf(RuiSedi.class);
    }

    @Test
    void mapperRuiCollaboratori_wrongStringArray_doesNotMapLine() {
        CustomLineMapper<RuiCollaboratori> customLineMapper = new CustomLineMapper<>(
            RuiCollaboratori.class);
        LineMapper<RuiCollaboratori> mapper = customLineMapper.mapper("any", "any");
        assertThrows(BindException.class, () -> mapper.mapLine("A;", 1));
    }

    @Test
    void mapperRuiCollaboratori_correctStringArray_doesMapLine() throws Exception {
        CustomLineMapper<RuiCollaboratori> customLineMapper = new CustomLineMapper<>(
            RuiCollaboratori.class);
        LineMapper<RuiCollaboratori> mapper = customLineMapper.mapper("oss", "livello",
                                                                      "numeroIscrittoIntermediario",
                                                                      "numeroIscrittoCollaboratoriPrimoLivello",
                                                                      "numeroIscrittoCollaboratoriSecondoLivello",
                                                                      "qualificaRapporto");
        assertThat(mapper.mapLine("0;I;MOCK_INT;MOCK_I_LVL;;MOCK_QUALIFICA", 1)).isInstanceOf(
            RuiCollaboratori.class);
    }

}
