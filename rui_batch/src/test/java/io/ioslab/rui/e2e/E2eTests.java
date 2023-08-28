package io.ioslab.rui.e2e;

import static io.ioslab.rui.batch.utility.Costants.PARAMETER_DATE_CSV;
import static io.ioslab.rui.utils.TestConstants.VALID_RECORD_PER_CSV;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

import io.ioslab.rui.batch.RuiBatchApplication;
import io.ioslab.rui.batch.utility.SendEmailError;
import io.ioslab.rui.utils.TestConstants;
import io.ioslab.rui.utils.TestConstants.DomainObjectEnumerator;
import java.io.IOException;
import java.nio.file.Files;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.MockedStatic;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = RuiBatchApplication.class)
@ActiveProfiles("test")
@SpringBatchTest
@TestPropertySource(properties = "spring.batch.job.enabled=false")
class E2eTests {

    @Autowired
    DataSource dataSource;
    Resource configTest = new ClassPathResource("config-test.ini");
    Resource testZip = new ClassPathResource(
        "zipsForIntegration/VALID_DATA_WITH_WANTED_AND_FAILING.zip");

    @Test
    void e2e_withConfigAndDate_doesFailJob() throws IOException {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            String[] args = new String[4];
            args[0] = configTest.getFile().getPath();
            args[1] = "-" + PARAMETER_DATE_CSV + "=" +
                      TestConstants.CSV_DATE_AS_VALID_JOB_PARAMETER;
            args[2] = "";
            args[3] = "";
            RuiBatchApplication.main(args);
            sendEmailErrorMockedStatic.verify(
                () -> SendEmailError.manageError("percorsoInput e url non presenti!"), times(1));
        }
    }

    @Test
    void e2e_withOnlyConfig_doesFailJob() throws IOException {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            RuiBatchApplication.main(new String[]{configTest.getFile().getPath()});
            sendEmailErrorMockedStatic.verify(
                () -> SendEmailError.manageError("percorsoInput e url non presenti!"), times(1));
        }
    }

    @ParameterizedTest
    @EnumSource(DomainObjectEnumerator.class)
    void e2e_withInput_doesInsertValidWanted(DomainObjectEnumerator value) throws IOException {
        String[] args = new String[4];
        args[0] = configTest.getFile().getPath();
        args[1] = "-dataCsv=" + TestConstants.CSV_DATE_AS_VALID_JOB_PARAMETER;
        args[2] = "-percorsoInput=" + testZip.getFile().getPath();
        args[3] = "-percorsoOutput=" + Files.createTempDirectory("testOut").toFile().getPath();
        RuiBatchApplication.main(args);
        assertThat(new JdbcTemplate(dataSource).queryForObject(value.getSqlCountStatement(),
                                                               Integer.class)).isEqualTo(
            VALID_RECORD_PER_CSV);
    }

}