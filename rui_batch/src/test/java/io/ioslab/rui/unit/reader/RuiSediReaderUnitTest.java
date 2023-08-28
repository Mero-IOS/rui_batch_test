package io.ioslab.rui.unit.reader;

import static io.ioslab.rui.utils.TestConstants.CSV_DATE_AS_VALID_JOB_PARAMETER;
import static io.ioslab.rui.utils.TestConstants.CSV_FILE_NAME_DATE;
import static io.ioslab.rui.utils.TestConstants.FAILING_RECORD_PER_CSV;
import static io.ioslab.rui.utils.TestConstants.FAILING_RECORD_PER_FAILING_CSV;
import static io.ioslab.rui.utils.TestConstants.VALID_RECORD_PER_CSV;
import static io.ioslab.rui.utils.TestUtils.getExecutionContext;
import static io.ioslab.rui.utils.TestUtils.mockChunkContext;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.ioslab.rui.batch.reader.RuiSediReader;
import io.ioslab.rui.common.model.rui.RuiSedi;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class RuiSediReaderUnitTest {

    private FlatFileItemReader<RuiSedi> flatFileItemReader;
    private ChunkContext chunkContextMock;
    private final Resource mockCsvWithValidAndInvalidRecords = new ClassPathResource(
        "mockCsv/mockCsvWithValidAndInvalidRecords");
    private final Resource mockCsvWithOnlyFailingRecords = new ClassPathResource(
        "mockCsv/mockCsvWithOnlyFailingRecords");

    @BeforeEach
    void setFlatFileItemReader() throws IOException {
        flatFileItemReader = new RuiSediReader().readerRuiSedi(
            mockCsvWithValidAndInvalidRecords.getFile().getPath(), CSV_DATE_AS_VALID_JOB_PARAMETER);
        chunkContextMock = mockChunkContext();
    }

    @Test
    void readerRuiSedi_invalidCsvDate_throwsExceptionInOpening() throws IOException {
        flatFileItemReader = new RuiSediReader().readerRuiSedi(
            mockCsvWithValidAndInvalidRecords.getFile().getPath(), "");
        ExecutionContext executionContext = getExecutionContext(chunkContextMock);
        assertThrows(ItemStreamException.class, () -> flatFileItemReader.open(executionContext));
    }

    @Test
    void readerRuiSedi_fromValidCsv_doesRead() throws Exception {
        flatFileItemReader.afterPropertiesSet();
        flatFileItemReader.open(getExecutionContext(chunkContextMock));
        assertThat(flatFileItemReader.read()).isInstanceOf(RuiSedi.class);
    }

    @Test
    void readerRuiSedi_fromValidCsv_getsValidRecords() throws Exception {
        List<RuiSedi> readSedi = new ArrayList<>();
        flatFileItemReader.afterPropertiesSet();
        flatFileItemReader.open(getExecutionContext(chunkContextMock));
        for (int i = 0; i < VALID_RECORD_PER_CSV; i++) {
            readSedi.add(flatFileItemReader.read());
        }
        assertThat(readSedi.size()).isEqualTo(VALID_RECORD_PER_CSV);
    }

    @Test
    void readerRuiSedi_fromValidCsv_throwsExceptionOnLastNonValidIgnoringDoubleReturn()
        throws Exception {
        flatFileItemReader.afterPropertiesSet();
        flatFileItemReader.open(getExecutionContext(chunkContextMock));
        for (int i = 0; i < VALID_RECORD_PER_CSV; i++) {
            flatFileItemReader.read();
        }
        int lastIndex = 0;
        while (true) {
            try {
                if (flatFileItemReader.read() == null) {
                    break;
                }
            } catch (FlatFileParseException e) {
                lastIndex = e.getLineNumber() - 1; //HEADER LINE SKIP OFFSET
                System.out.println("LINE THROWING EXCEPTION: " + e.getLineNumber());
            }
        }
        assertThat(lastIndex).isEqualTo(VALID_RECORD_PER_CSV + FAILING_RECORD_PER_CSV);
    }

    @Test
    void readerRuiSedi_fromElevenInvalidRecordCsv_throwsElevenExceptions() throws Exception {
        flatFileItemReader = new RuiSediReader().readerRuiSedi(
            mockCsvWithOnlyFailingRecords.getFile().getPath(), CSV_FILE_NAME_DATE);
        flatFileItemReader.afterPropertiesSet();
        flatFileItemReader.open(getExecutionContext(chunkContextMock));
        int lastIndex = 0;
        while (true) {
            try {
                if (flatFileItemReader.read() == null) {
                    break;
                }
            } catch (FlatFileParseException e) {
                lastIndex = e.getLineNumber() - 1; //HEADER LINE SKIP OFFSET
                System.out.println("LINE THROWING EXCEPTION: " + e.getLineNumber());
            }
        }
        assertThat(lastIndex).isEqualTo(FAILING_RECORD_PER_FAILING_CSV);
    }

}
