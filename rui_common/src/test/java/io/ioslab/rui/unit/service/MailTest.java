package io.ioslab.rui.unit.service;

import static io.ioslab.rui.common.utility.Costants.INI_MAIL_SECTION;
import static io.ioslab.rui.common.utility.Costants.INI_MYSQL_SECTION;
import static io.ioslab.rui.common.utility.Costants.INI_SMTP_SECTION;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.ioslab.rui.common.service.ini.IniReader;
import io.ioslab.rui.common.service.mail.Mail;
import java.io.File;
import java.io.IOException;
import org.ini4j.Wini;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MailTest {

    Mail testMail;

    @BeforeEach
    void setTestMail() {
        testMail = new Mail();
    }

    @Test
    void sendMail_withStrings_doesThrowNullPointerException() {
        assertThrows(NullPointerException.class,
                     () -> testMail.sendMail("TEST_SUBJECT", "TEST_BODY",
                                             IniReader.getIniReaderder()));
    }

    @Test
    void sendMail_withEmptyToSection_doesThrowNullPointerException() throws IOException {
        File iniFile = File.createTempFile("testIni", ".ini");
        Wini iniTest = new Wini(iniFile);
        iniTest.put(INI_SMTP_SECTION, "port", "25");
        iniTest.put(INI_MAIL_SECTION, "from", "TEST_FROM");
        iniTest.store();
        IniReader.setIniReader(iniFile.getPath());
        assertThrows(NullPointerException.class,
                     () -> testMail.sendMail("TEST_SUBJECT", "TEST_BODY",
                                             IniReader.getIniReaderder()));
    }

}
