package io.ioslab.rui.common.model.ini;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MailModel {

    private String from;
    private String to;
    private String subject;
    private String text;
}
