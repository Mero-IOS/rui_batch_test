package io.ioslab.rui.common.model.rui;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class Rui {

    @Column(name = "oss")
    private long id;

    @Column(name = "data_elaborazione")
    private Date dataElaborazione;
}
