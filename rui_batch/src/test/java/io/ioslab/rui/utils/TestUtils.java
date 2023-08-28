

package io.ioslab.rui.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.batch.test.MetaDataInstanceFactory.createJobExecution;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.Resource;

public class TestUtils {

    public static ChunkContext mockChunkContext() {
        ChunkContext chunkContextMock = mock(ChunkContext.class);
        StepExecution stepExecutionMock = mock(StepExecution.class);
        StepContext stepContextMock = mock(StepContext.class);
        JobExecution jobExecution = createJobExecution();
        when(chunkContextMock.getStepContext()).thenReturn(stepContextMock);
        when(stepContextMock.getStepExecution()).thenReturn(stepExecutionMock);
        when(stepExecutionMock.getJobExecution()).thenReturn(jobExecution);
        return chunkContextMock;
    }

    public static ExecutionContext getExecutionContext(ChunkContext context) {
        return context.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
    }

    public static Path makeTempDuplicateOfResource(Resource testResource) throws IOException {
        Path tempOut = Files.createTempDirectory(testResource.getFile().getName() + UUID.randomUUID());
        File[] testResourceContents = testResource.getFile().listFiles();
        if (testResourceContents != null) {
            for (File fileInsideTestResourceExcludingScarti : testResourceContents) {
                if (!fileInsideTestResourceExcludingScarti.getName().contains("scarti")){
                    Path destinationPath = tempOut.resolve(fileInsideTestResourceExcludingScarti.getName());
                    Files.copy(fileInsideTestResourceExcludingScarti.toPath(), destinationPath);
                }
            }
        }
        return tempOut;
    }

}
