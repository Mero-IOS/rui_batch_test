package io.ioslab.rui.common.service.mail;

import io.ioslab.rui.common.service.ini.IniReader;

public class Mail {

    public void sendMail(String subject, String bodyMail, IniReader iniReader) {
        MailSender
                .getSender(iniReader)
                .send(MailContent.getMail(subject, bodyMail, iniReader));
    }
}
