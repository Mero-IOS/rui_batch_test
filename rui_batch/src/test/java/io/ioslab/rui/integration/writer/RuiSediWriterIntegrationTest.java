package io.ioslab.rui.integration.writer;

import static io.ioslab.rui.utils.TestConstants.COUNT_MANDATI_SQL;
import static io.ioslab.rui.utils.TestConstants.COUNT_SEDI_SQL;
import static io.ioslab.rui.utils.TestConstants.CSV_DATE_AS_SQL_DATE;
import static io.ioslab.rui.utils.TestConstants.VALID_RECORD_PER_CSV;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;

import io.ioslab.rui.batch.datasource.CustomDataSource;
import io.ioslab.rui.batch.writer.RuiSediWriter;
import io.ioslab.rui.common.model.rui.RuiSedi;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
class RuiSediWriterIntegrationTest {

    private JdbcBatchItemWriter<RuiSedi> writer;
    @Autowired
    private DataSource dataSource;

    private List<RuiSedi> sediList;

    @BeforeEach
    void setWriter() throws SQLException {
        dataSource = new CustomDataSource().getDataSourceTest();
        writer = new RuiSediWriter().writerRuiSedi(dataSource);
        writer.afterPropertiesSet();
        setList();
    }

    @Test
    void write_AnyList_doesWriteToDatabase() throws Exception {
        writer.write(sediList);
        assertThat(new JdbcTemplate(dataSource).queryForObject(COUNT_SEDI_SQL,
                                                               Integer.class)).isEqualTo(
            VALID_RECORD_PER_CSV);
    }

    private void setList() {
        sediList = new ArrayList<>();
        for (int i = 0; i < VALID_RECORD_PER_CSV; i++) {
            RuiSedi toAdd = new RuiSedi();
            toAdd.setId(i);
            toAdd.setDataElaborazione(CSV_DATE_AS_SQL_DATE);
            sediList.add(toAdd);
        }
    }

}
