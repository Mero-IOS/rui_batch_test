package io.ioslab.rui.batch.listener;

import io.ioslab.rui.batch.utility.SendEmailError;
import io.ioslab.rui.common.model.rui.Rui;
import lombok.Builder;
import org.springframework.batch.core.ItemProcessListener;

@Builder
public class ProcessListener implements ItemProcessListener<Rui, Rui> {

    private String fileName;

    @Override
    public void beforeProcess(Rui rui) {
    }

    @Override
    public void afterProcess(Rui rui, Rui rui2) {
    }

    @Override
    public void onProcessError(Rui rui, Exception e) {
        SendEmailError.manageError("errore nel processare il file: " + fileName);
    }
}
