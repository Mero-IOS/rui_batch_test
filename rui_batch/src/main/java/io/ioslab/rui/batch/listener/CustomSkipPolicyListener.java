package io.ioslab.rui.batch.listener;

import io.ioslab.rui.batch.exception.MailErrorException;
import io.ioslab.rui.common.model.rui.Rui;
import org.springframework.batch.core.SkipListener;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class CustomSkipPolicyListener implements SkipListener<Rui, Rui> {

    private final Path file;

    public CustomSkipPolicyListener(String outputPath) {
        this.file = Paths.get(outputPath + File.separator + "scarti.txt");
        if (Files.notExists(file))
            createWasteFile(file);
    }

    private void createWasteFile(Path file) {
        try {
            Files.createFile(file);
        } catch (IOException e) {
            throw new MailErrorException("impossibile creare il file di scarti", e.getMessage());
        }
    }

    @Override
    public void onSkipInRead(Throwable t) {
        writeLineInWasteFile("reader", file, null);
    }

    @Override
    public void onSkipInWrite(Rui item, Throwable t) {
        writeLineInWasteFile("writer", file, item);
    }

    @Override
    public void onSkipInProcess(Rui item, Throwable t) {
        writeLineInWasteFile("process", file, item);
    }

    private void writeLineInWasteFile(String location, Path file, Rui ruiObject) {
        if (Objects.isNull(ruiObject))
            writeLineInWasteFileWithoutObject(location, file);
        else
            writeLineInWasteFileWithObject(location, file, ruiObject);
    }

    private void writeLineInWasteFileWithoutObject(String location, Path file) {
        try {
            Files.write(file,
                    (location + " - " + "\n").getBytes(Charset.defaultCharset()),
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new MailErrorException("impossibile scrivere sul file scarti", e.getMessage());
        }
    }

    private void writeLineInWasteFileWithObject(String location, Path file, Rui ruiObject) {
        try {
            Files.write(file,
                    (location + " - " + ruiObject.toString() + "\n").getBytes(Charset.defaultCharset()),
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new MailErrorException("impossibile scrivere sul file scarti", e.getMessage());
        }
    }
}
