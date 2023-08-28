package io.ioslab.rui.batch.utility;

import io.ioslab.rui.batch.exception.MailErrorException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Casting {

    private Casting() {
    }

    public static Date castStringToDate(String date) {
        if (Objects.isNull(date) || date.isEmpty())
            return null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setLenient(false);
        try {
            return simpleDateFormat.parse(date);
        } catch (Exception e) {
            throw new MailErrorException("data non valida", e.getMessage());
        }
    }
}
