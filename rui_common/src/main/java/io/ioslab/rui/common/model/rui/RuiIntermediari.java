package io.ioslab.rui.common.model.rui;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "rui_intermediari")
public class RuiIntermediari extends Rui {

    @Column(name = "inoperativo")
    private Integer inoperativo;

    @Column(name = "data_inizio_inoperativita")
    private Date dataInizioInoperativita;

    @Column(name = "numero_iscrizione_rui")
    private String numeroIscrizioneRui;

    @Column(name = "data_iscrizione")
    private Date dataIscrizione;

    @Column(name = "cognome_nome")
    private String cognomeNome;

    @Column(name = "stato")
    private String stato;

    @Column(name = "comune_nascita")
    private String comuneNascita;

    @Column(name = "data_nascita")
    private Date dataNascita;

    @Column(name = "ragione_sociale")
    private String ragioneSociale;

    @Column(name = "provincia_nascita")
    private String provinciaNascita;

    @Column(name = "titolo_individuale_sez_a")
    private String titoloIndividialeSezioneA;

    @Column(name = "attivita_esercitata_sez_a")
    private String attivitaEsericitataSezioneA;

    @Column(name = "titolo_individuale_sez_b")
    private String titoloIndividialeSezioneB;

    @Column(name = "attivita_esercitata_sez_b")
    private String attivitaEsericitataSezioneB;
}
