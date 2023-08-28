package io.ioslab.rui.common.model.rui;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "rui_cariche")
public class RuiCariche extends Rui {

    @Column(name = "numero_iscrizione_rui_pf")
    private String numeroIscrizioneRuiPf;

    @Column(name = "numero_iscrizione_rui_pg")
    private String numeroIscrizioneRuiPg;

    @Column(name = "qualifica_intermediario")
    private String qualificaIntermediario;
}
