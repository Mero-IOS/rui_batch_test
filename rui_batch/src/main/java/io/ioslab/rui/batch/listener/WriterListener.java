package io.ioslab.rui.batch.listener;

import io.ioslab.rui.batch.utility.SendEmailError;
import io.ioslab.rui.common.model.rui.Rui;
import lombok.Builder;
import org.springframework.batch.core.ItemWriteListener;

import java.util.List;

@Builder
public class WriterListener implements ItemWriteListener<Rui> {

    private String fileName;

    @Override
    public void beforeWrite(List<? extends Rui> items) {
    }

    @Override
    public void afterWrite(List<? extends Rui> items) {
    }

    @Override
    public void onWriteError(Exception exception, List<? extends Rui> items) {
        SendEmailError.manageError("errore durante la scrittura del file: " + fileName);
    }
}
