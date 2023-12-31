package io.ioslab.rui.integration.writer;

import static io.ioslab.rui.utils.TestConstants.COUNT_MANDATI_SQL;
import static io.ioslab.rui.utils.TestConstants.CSV_DATE_AS_SQL_DATE;
import static io.ioslab.rui.utils.TestConstants.VALID_RECORD_PER_CSV;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.ioslab.rui.batch.datasource.CustomDataSource;
import io.ioslab.rui.batch.writer.RuiMandatiWriter;
import io.ioslab.rui.common.model.rui.RuiMandati;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("test")
@SpringBootTest(classes = {CustomDataSource.class})
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class RuiMandatiWriterIntegrationTest {

    private JdbcBatchItemWriter<RuiMandati> writer;
    @Autowired
    private DataSource dataSource;

    private List<RuiMandati> mandatiList;

    @BeforeEach
    void setWriter() {
        dataSource = new CustomDataSource().getDataSourceTest();
        writer = new RuiMandatiWriter().writerRuiMandati(dataSource);
        writer.afterPropertiesSet();
        setList();
    }

    @Test
    void write_AnyList_doesWriteToDatabase() throws Exception {
        writer.write(mandatiList);
        assertThat(new JdbcTemplate(dataSource).queryForObject(COUNT_MANDATI_SQL,
                                                               Integer.class)).isEqualTo(
            VALID_RECORD_PER_CSV);
    }

    private void setList() {
        mandatiList = new ArrayList<>();
        for (int i = 0; i < VALID_RECORD_PER_CSV; i++) {
            RuiMandati toAdd = new RuiMandati();
            toAdd.setId(i);
            toAdd.setDataElaborazione(CSV_DATE_AS_SQL_DATE);
            mandatiList.add(toAdd);
        }
    }
}
