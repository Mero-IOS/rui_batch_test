package io.ioslab.rui.batch.listener;

import io.ioslab.rui.batch.utility.SendEmailError;
import io.ioslab.rui.common.model.rui.Rui;
import lombok.Builder;
import org.springframework.batch.core.ItemReadListener;

@Builder
public class ReaderListener implements ItemReadListener<Rui> {

    private String fileName;

    @Override
    public void beforeRead() {
    }

    @Override
    public void afterRead(Rui rui) {
    }

    @Override
    public void onReadError(Exception e) {
        SendEmailError.manageError("errore durante la lettura del file: " + fileName);
    }
}
