package io.ioslab.rui.batch.policies;

import io.ioslab.rui.batch.exception.MailErrorException;
import lombok.Builder;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

@Builder
public class CustomSkipPolicy implements SkipPolicy {

    private static final int MAX_SKIP_COUNT = 10;

    @Override
    public boolean shouldSkip(Throwable throwable, int i) throws SkipLimitExceededException {
        if (i > MAX_SKIP_COUNT)
            throw new MailErrorException("numero massimo di righe saltate raggiunto");
        return true;
    }
}
