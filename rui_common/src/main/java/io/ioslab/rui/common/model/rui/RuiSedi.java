package io.ioslab.rui.common.model.rui;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "rui_sedi")
public class RuiSedi extends Rui {

    @Column(name = "numero_iscrizione_int")
    private String numeroIscrizioneInt;

    @Column(name = "tipo_sede")
    private String tipoSede;

    @Column(name = "comune_sede")
    private String comuneSede;

    @Column(name = "provincia_sede")
    private String provinciaSede;

    @Column(name = "cap_sede")
    private String capSede;

    @Column(name = "indirizzo_sede")
    private String indirizzoSede;
}
