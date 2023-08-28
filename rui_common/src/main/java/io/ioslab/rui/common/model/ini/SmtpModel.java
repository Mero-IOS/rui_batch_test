package io.ioslab.rui.common.model.ini;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SmtpModel {

    private String host;
    private int port;
    private String username;
    private String password;
    private String protocol;
}
