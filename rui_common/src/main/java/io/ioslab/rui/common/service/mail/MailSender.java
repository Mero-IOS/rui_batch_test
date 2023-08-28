package io.ioslab.rui.common.service.mail;

import io.ioslab.rui.common.model.ini.SmtpModel;
import io.ioslab.rui.common.service.ini.IniReader;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class MailSender {

    private MailSender() {
    }

    protected static JavaMailSenderImpl getSender(IniReader iniReader) {
        SmtpModel smtpModel = iniReader.getSmtpModelFromIni();
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(smtpModel.getHost());
        sender.setPort(smtpModel.getPort());
        sender.setProtocol(smtpModel.getProtocol());
        sender.setUsername(smtpModel.getUsername());
        sender.setPassword(smtpModel.getPassword());
        return sender;
    }
}
