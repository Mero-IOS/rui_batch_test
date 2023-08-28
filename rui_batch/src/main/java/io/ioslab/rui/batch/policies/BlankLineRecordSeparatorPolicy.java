package io.ioslab.rui.batch.policies;

import lombok.NonNull;
import org.springframework.batch.item.file.separator.SimpleRecordSeparatorPolicy;

public class BlankLineRecordSeparatorPolicy extends SimpleRecordSeparatorPolicy {

    @Override
    public boolean isEndOfRecord(String line) {
        return line.trim().length() != 0 && super.isEndOfRecord(line);
    }

    @Override
    public String postProcess(@NonNull String line) {
        if (line.trim().length() == 0)
            return null;

        return super.postProcess(line);
    }
}
