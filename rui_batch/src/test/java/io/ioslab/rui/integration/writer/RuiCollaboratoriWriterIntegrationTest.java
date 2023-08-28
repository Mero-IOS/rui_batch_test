package io.ioslab.rui.integration.writer;

import static io.ioslab.rui.utils.TestConstants.COUNT_CARICHE_SQL;
import static io.ioslab.rui.utils.TestConstants.COUNT_COLLABORATORI_SQL;
import static io.ioslab.rui.utils.TestConstants.CSV_DATE_AS_SQL_DATE;
import static io.ioslab.rui.utils.TestConstants.VALID_RECORD_PER_CSV;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;

import io.ioslab.rui.batch.datasource.CustomDataSource;
import io.ioslab.rui.batch.writer.RuiCollaboratoriWriter;
import io.ioslab.rui.common.model.rui.RuiCollaboratori;
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
class RuiCollaboratoriWriterIntegrationTest {

    private JdbcBatchItemWriter<RuiCollaboratori> writer;
    private DataSource dataSource;
    private List<RuiCollaboratori> collaboratoriList;

    @BeforeEach
    void setWriter() throws SQLException {
        dataSource = new CustomDataSource().getDataSourceTest();
        writer = new RuiCollaboratoriWriter().writerRuiCollaboratori(dataSource);
        writer.afterPropertiesSet();
        createList();
    }

    @Test
    void write_AnyList_doesWriteToDatabase() throws Exception {
        writer.write(collaboratoriList);
        assertThat(new JdbcTemplate(dataSource).queryForObject(COUNT_COLLABORATORI_SQL, Integer.class)).isEqualTo(
            VALID_RECORD_PER_CSV);
    }

    private void createList() {
        collaboratoriList = new ArrayList<>();
        for (int i = 0; i < VALID_RECORD_PER_CSV; i++) {
            RuiCollaboratori toAdd = new RuiCollaboratori();
            toAdd.setId(i);
            toAdd.setDataElaborazione(CSV_DATE_AS_SQL_DATE);
            collaboratoriList.add(toAdd);
        }
    }

}
