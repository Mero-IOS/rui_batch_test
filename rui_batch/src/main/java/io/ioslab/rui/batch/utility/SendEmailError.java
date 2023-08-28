package io.ioslab.rui.batch.utility;

import io.ioslab.rui.common.service.ini.IniReader;
import io.ioslab.rui.common.service.mail.Mail;
import org.springframework.mail.MailException;

public class SendEmailError {

    private SendEmailError() {
    }

    public static void manageError(String error) {
        Mail mail = new Mail();
        try {
            mail.sendMail(
                    "IVASS - Batch error",
                    error,
                    IniReader.getIniReaderder());
        } catch (MailException e) {
            e.printStackTrace();
        }
    }

    public static void manageError(String error, String stacktrace) {
        Mail mail = new Mail();
        try {
            mail.sendMail(
                    "IVASS - Batch error",
                    createMailBody(error, stacktrace),
                    IniReader.getIniReaderder());
        } catch (MailException e) {
            e.printStackTrace();
        }
    }

    private static String createMailBody(String error, String stacktrace) {
        return "errore: \n" + error
                + "\n---\n"
                + "stacktrace: \n" + stacktrace;
    }
}
