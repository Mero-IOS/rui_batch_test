package io.ioslab.rui.unit.utility;

import static io.ioslab.rui.batch.utility.Costants.PARAMETER_DATE_CSV;
import static io.ioslab.rui.batch.utility.Costants.PARAMETER_INPUT_PATH;
import static io.ioslab.rui.batch.utility.Costants.PARAMETER_OUTPUT_PATH;
import static io.ioslab.rui.batch.utility.Costants.PARAMETER_URL;
import static io.ioslab.rui.utils.TestUtils.mockChunkContext;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.ioslab.rui.batch.utility.InsertParametersInContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.scope.context.ChunkContext;

class InsertParametersInContextUnitTest {

    ChunkContext chunkContextMock;
    JobExecution jobExecution;
    InsertParametersInContext insertParametersInContextTest;

    @BeforeEach
    void setChunkContext() {
        chunkContextMock = mockChunkContext();
        jobExecution = chunkContextMock.getStepContext().getStepExecution().getJobExecution();
        insertParametersInContextTest = new InsertParametersInContext(chunkContextMock);
    }

    @Test
    void putUrlInContext_AnyString_doesPutAnyStringInUrl() {
        insertParametersInContextTest.putUrlInContext("ANY_STRING");
        assertThat(jobExecution.getExecutionContext().getString(PARAMETER_URL)).isEqualTo(
            "ANY_STRING");
    }

    @Test
    void putInputPathInContext_AnyString_doesPutAnyStringInInputPath() {
        insertParametersInContextTest.putInputPathInContext("ANY_STRING");
        assertThat(jobExecution.getExecutionContext().getString(PARAMETER_INPUT_PATH)).isEqualTo(
            "ANY_STRING");
    }

    @Test
    void putDateCsvInContext_AnyString_doesPutAnyStringInDateCsv() {
        insertParametersInContextTest.putDateCsvInContext("ANY_STRING");
        assertThat(jobExecution.getExecutionContext().getString(PARAMETER_DATE_CSV)).isEqualTo(
            "ANY_STRING");
    }

    @Test
    void putOutputPathInContext_AnyString_returnDoesContainAnyString() throws IOException {
        Path outputPath = Files.createTempDirectory("TEST");
        insertParametersInContextTest.putOutputPathInContext(outputPath.toString(), "DATE");
        assertThat(jobExecution.getExecutionContext().getString(PARAMETER_OUTPUT_PATH)).contains(
            outputPath.toString());
    }

    @Test
    void putOutputPathInContext_AnyString_createsOutputDirectory() throws IOException {

        Path outputPath = Files.createTempDirectory("TEST");
        Files.delete(outputPath);
        String outputPathString = outputPath.toString();
        String date = "DATE";
        String outputDirectoryPath = outputPathString + File.separator + "rui_ivass" +
                                     File.separator + date;
        insertParametersInContextTest.putOutputPathInContext(outputPathString, "DATE");
        assertThat(new File(outputDirectoryPath)).exists();
    }

    @Test
    void putOutputPathInContext_AnyString_doesPutAnyStringInOutputPath() throws IOException {
        Path outputPath = Files.createTempDirectory("TEST");
        String outputPathString = outputPath.toString();
        String date = "DATE";
        insertParametersInContextTest.putOutputPathInContext(outputPathString, "DATE");
        assertThat(jobExecution.getExecutionContext().getString(PARAMETER_OUTPUT_PATH)).contains(
            outputPathString + File.separator + "rui_ivass" + File.separator + date);
    }

}
