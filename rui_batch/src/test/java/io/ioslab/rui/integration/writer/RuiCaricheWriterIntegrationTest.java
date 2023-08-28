package io.ioslab.rui.integration.writer;

import static io.ioslab.rui.utils.TestConstants.COUNT_CARICHE_SQL;
import static io.ioslab.rui.utils.TestConstants.CSV_DATE_AS_SQL_DATE;
import static io.ioslab.rui.utils.TestConstants.VALID_RECORD_PER_CSV;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;

import io.ioslab.rui.batch.datasource.CustomDataSource;
import io.ioslab.rui.batch.writer.RuiCaricheWriter;
import io.ioslab.rui.common.model.rui.RuiCariche;
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
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@ActiveProfiles("test")
@SpringBootTest(classes = {CustomDataSource.class})
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class RuiCaricheWriterIntegrationTest {

    private JdbcBatchItemWriter<RuiCariche> writer;

    @Autowired
    private DataSource dataSource;
    private List<RuiCariche> caricheList;

    @BeforeEach
    void setWriter() throws SQLException {
        writer = new RuiCaricheWriter().writerRuiCariche(dataSource);
        writer.afterPropertiesSet();
        createList();
    }

    @Test
    void write_AnyList_doesWriteToDatabase() throws Exception {
        writer.write(caricheList);
        assertThat(new JdbcTemplate(dataSource).queryForObject(COUNT_CARICHE_SQL, Integer.class)).isEqualTo(
            VALID_RECORD_PER_CSV);
    }

    private void createList() {
        caricheList = new ArrayList<>();
        for (int i = 0; i < VALID_RECORD_PER_CSV; i++) {
            RuiCariche toAdd = new RuiCariche();
            toAdd.setId(i);
            toAdd.setDataElaborazione(CSV_DATE_AS_SQL_DATE);
            caricheList.add(toAdd);
        }
    }

}
