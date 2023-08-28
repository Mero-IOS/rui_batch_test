package io.ioslab.rui.common.model.rui;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "rui_collaboratori")
public class RuiCollaboratori extends Rui {

    @Column(name = "livello")
    private String livello;

    @Column(name = "num_iscr_intermediario")
    private String numeroIscrittoIntermediario;

    @Column(name = "num_iscr_collaboratori_i_liv")
    private String numeroIscrittoCollaboratoriPrimoLivello;

    @Column(name = "num_iscr_collaboratori_ii_liv")
    private String numeroIscrittoCollaboratoriSecondoLivello;

    @Column(name = "qualifica_rapporto")
    private String qualificaRapporto;
}
