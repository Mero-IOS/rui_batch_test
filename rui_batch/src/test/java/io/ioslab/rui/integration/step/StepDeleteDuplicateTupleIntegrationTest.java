package io.ioslab.rui.integration.step;

import static io.ioslab.rui.batch.utility.Costants.PARAMETER_DATE_CSV;
import static io.ioslab.rui.utils.TestConstants.COUNT_COLLABORATORI_SQL;
import static io.ioslab.rui.utils.TestConstants.COUNT_INTERMEDIARI_SQL;
import static io.ioslab.rui.utils.TestConstants.COUNT_MANDATI_SQL;
import static io.ioslab.rui.utils.TestConstants.COUNT_SEDI_SQL;
import static io.ioslab.rui.utils.TestConstants.COUNT_CARICHE_SQL;
import static io.ioslab.rui.utils.TestConstants.CSV_DATE_AS_SQL_DATE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import io.ioslab.rui.batch.RuiBatchApplication;
import io.ioslab.rui.batch.tasklet.DeleteDuplicateTupleTasklet;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("test")
@SpringBootTest(classes = {RuiBatchApplication.class})
@SpringBatchTest
@Sql(scripts = "classpath:insertForDeleteDuplicateTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@TestPropertySource(properties = "spring.batch.job.enabled=false")
class StepDeleteDuplicateTupleIntegrationTest {

    @Autowired
    Job ruiBatchJob;

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    ExecutionContext executionContext;

    @BeforeEach
    void setContext() {
        executionContext = new ExecutionContext();
        executionContext.put(PARAMETER_DATE_CSV, CSV_DATE_AS_SQL_DATE.toString());
    }

    @Test
    void execute_withDate_doesComplete() {
        JobExecution shouldComplete = jobLauncherTestUtils.launchStep("deleteDuplicateTupleStep",
                                                                      executionContext);
        assertThat(shouldComplete.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
    }

    @Test
    void execute_SameDateAsSqlScript_doesRemoveAllCaricheFromDatabase() throws Exception {
        jobLauncherTestUtils.launchStep("deleteDuplicateTupleStep",
                                                                      executionContext);
        try (Connection connection = dataSource.getConnection();
             Statement countStatement = connection.createStatement()) {
            ResultSet resultSet = countStatement.executeQuery(COUNT_CARICHE_SQL);
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                assertThat(count).isZero();
            } else {
                fail("La tabella non esiste.");
            }
        }
    }

    @Test
    void execute_SameDateAsSqlScript_doesRemoveAllMandatiFromDatabase() throws Exception {
        jobLauncherTestUtils.launchStep("deleteDuplicateTupleStep",
                                                                      executionContext);
        try (Connection connection = dataSource.getConnection();
             Statement countStatement = connection.createStatement()) {
            ResultSet resultSet = countStatement.executeQuery(COUNT_MANDATI_SQL);
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                assertThat(count).isZero();
            } else {
                fail("La tabella non esiste.");
            }
        }
    }

    @Test
    void execute_SameDateAsSqlScript_doesRemoveAllSediFromDatabase() throws Exception {
        jobLauncherTestUtils.launchStep("deleteDuplicateTupleStep",
                                                                      executionContext);
        try (Connection connection = dataSource.getConnection();
             Statement countStatement = connection.createStatement()) {
            ResultSet resultSet = countStatement.executeQuery(COUNT_SEDI_SQL);
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                assertThat(count).isZero();
            } else {
                fail("La tabella non esiste.");
            }
        }
    }

    @Test
    void execute_SameDateAsSqlScript_doesRemoveAllCollaboratoriFromDatabase() throws Exception {
        jobLauncherTestUtils.launchStep("deleteDuplicateTupleStep",
                                                                      executionContext);
        try (Connection connection = dataSource.getConnection();
             Statement countStatement = connection.createStatement()) {
            ResultSet resultSet = countStatement.executeQuery(COUNT_COLLABORATORI_SQL);
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                assertThat(count).isZero();
            } else {
                fail("La tabella non esiste.");
            }
        }
    }

    @Test
    void execute_SameDateAsSqlScript_doesRemoveAllIntermediariFromDatabase() throws Exception {
        jobLauncherTestUtils.launchStep("deleteDuplicateTupleStep",
                                                                      executionContext);
        try (Connection connection = dataSource.getConnection();
             Statement countStatement = connection.createStatement()) {
            ResultSet resultSet = countStatement.executeQuery(COUNT_INTERMEDIARI_SQL);
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                assertThat(count).isZero();
            } else {
                fail("La tabella non esiste.");
            }
        }
    }
    @Test
    @Sql(scripts = "/dropSchema.sql")
    @Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void execute_withDroppedTables_doesFail() {
        JobExecution shouldFail = jobLauncherTestUtils.launchStep("deleteDuplicateTupleStep",
                                                                      executionContext);
        assertThat(shouldFail.getExitStatus().getExitCode()).isEqualTo("FAILED");
    }

    @Test
    void execute_WithJdbcMock_doesThrowException() {
        DeleteDuplicateTupleTasklet tasklet;
        tasklet = DeleteDuplicateTupleTasklet.builder()
                                             .date("")
                                             .jdbcTemplate(new JdbcTemplate(mock(DataSource.class)))
                                             .build();
        assertThrows(CannotGetJdbcConnectionException.class, () -> tasklet.execute(null, null));
    }

}
