package io.ioslab.rui.batch.utility;

import java.io.File;

public class CreateFileNameForReader {

    private CreateFileNameForReader() {
    }

    public static String createFileName(String path, String fileName, String date) {
        return new StringBuilder(path)
                .append(File.separator)
                .append(fileName)
                .append("_")
                .append(date.replace("-", "_"))
                .append(".csv")
                .toString();
    }
}
