package io.ioslab.rui.batch.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SqlQuery {

    INSERT_RUI_CARICHE(
            "INSERT INTO `rui_cariche`" +
                    "(`oss`, `numero_iscrizione_rui_pf`, `numero_iscrizione_rui_pg`, `qualifica_intermediario`, `data_elaborazione`) " +
                    "VALUES " +
                    "(:id, :numeroIscrizioneRuiPf, :numeroIscrizioneRuiPg, :qualificaIntermediario, :dataElaborazione);"
    ),
    DELETE_RUI_CARICHE("DELETE FROM `rui_cariche` WHERE data_elaborazione=?;"),
    INSERT_RUI_COLLABORATORI(
            "INSERT INTO `rui_collaboratori` " +
                    "(`oss`, `livello`, `num_iscr_intermediario`, `num_iscr_collaboratori_i_liv`, `num_iscr_collaboratori_ii_liv`, `qualifica_rapporto`, `data_elaborazione`) " +
                    "VALUES " +
                    "(:id, :livello, :numeroIscrittoIntermediario, :numeroIscrittoCollaboratoriPrimoLivello, :numeroIscrittoCollaboratoriSecondoLivello, :qualificaRapporto, :dataElaborazione);"
    ),
    DELETE_RUI_COLLABORATORI("DELETE FROM `rui_collaboratori` WHERE data_elaborazione=?;"),
    INSERT_RUI_INTERMEDIARI(
            "INSERT INTO `rui_intermediari` " +
                    "(`oss`, `inoperativo`, `data_inizio_inoperativita`, `numero_iscrizione_rui`, `data_iscrizione`, `cognome_nome`, `stato`, `comune_nascita`, `data_nascita`, `ragione_sociale`, `provincia_nascita`, `titolo_individuale_sez_a`, `attivita_esercitata_sez_a`, `titolo_individuale_sez_b`, `attivita_esercitata_sez_b`, `data_elaborazione`) " +
                    "VALUES " +
                    "(:id, :inoperativo, :dataInizioInoperativita, :numeroIscrizioneRui, :dataIscrizione, :cognomeNome, :stato, :comuneNascita, :dataNascita, :ragioneSociale, :provinciaNascita, :titoloIndividialeSezioneA, :attivitaEsericitataSezioneA, :titoloIndividialeSezioneB, :attivitaEsericitataSezioneB, :dataElaborazione);"
    ),
    DELETE_RUI_INTERMEDIARI("DELETE FROM `rui_intermediari` WHERE data_elaborazione=?;"),
    INSERT_RUI_MANDATI(
            "INSERT INTO `rui_mandati` " +
                    "(`oss`, `numero_iscrizione_rui`, `codice_compagnia`, `ragione_sociale`, `data_elaborazione`) " +
                    "VALUES " +
                    "(:id, :numeroIscrizioneRui, :codiceCompagnia, :ragioneSociale, :dataElaborazione);"
    ),
    DELETE_RUI_MANDATI("DELETE FROM `rui_mandati` WHERE data_elaborazione=?;"),
    INSERT_RUI_SEDI(
            "INSERT INTO `rui_sedi` " +
                    "(`oss`, `numero_iscrizione_int`, `tipo_sede`, `comune_sede`, `provincia_sede`, `cap_sede`, `indirizzo_sede`, `data_elaborazione`) " +
                    "VALUES " +
                    "(:id, :numeroIscrizioneInt, :tipoSede, :comuneSede, :provinciaSede, :capSede, :indirizzoSede, :dataElaborazione);"
    ),
    DELETE_RUI_SEDI("DELETE FROM `rui_sedi` WHERE data_elaborazione=?;");
    private final String query;
}
