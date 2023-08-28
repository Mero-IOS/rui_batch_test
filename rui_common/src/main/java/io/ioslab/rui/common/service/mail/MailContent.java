package io.ioslab.rui.common.service.mail;

import io.ioslab.rui.common.model.ini.MailModel;
import io.ioslab.rui.common.service.ini.IniReader;
import org.springframework.mail.SimpleMailMessage;

public class MailContent {

    private MailContent() {
    }

    protected static SimpleMailMessage getMail(String subject, String bodyMail, IniReader iniReader) {
        MailModel mailModel = iniReader.getMailModelFromIni();
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(mailModel.getFrom());
        mail.setTo(mailModel.getTo());
        mail.setSubject(subject);
        mail.setText(bodyMail);
        return mail;
    }
}
