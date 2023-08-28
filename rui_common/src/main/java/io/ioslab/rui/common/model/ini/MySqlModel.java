package io.ioslab.rui.common.model.ini;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MySqlModel {

    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
