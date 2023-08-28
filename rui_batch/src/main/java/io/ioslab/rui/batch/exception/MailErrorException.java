package io.ioslab.rui.batch.exception;

import io.ioslab.rui.batch.utility.SendEmailError;

public class MailErrorException extends RuntimeException {

    public MailErrorException() {
        super();
    }

    public MailErrorException(String message) {
        super(message);
        SendEmailError.manageError(message);
    }

    public MailErrorException(String message, String stacktrace) {
        super(message);
        SendEmailError.manageError(message, stacktrace);
    }
}
