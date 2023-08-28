package io.ioslab.rui.common.model.rui;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "rui_mandanti")
public class RuiMandati extends Rui {

    @Column(name = "numero_iscrizione_rui")
    private String numeroIscrizioneRui;

    @Column(name = "codice_compagnia")
    private String codiceCompagnia;

    @Column(name = "ragione_sociale")
    private String ragioneSociale;
}
