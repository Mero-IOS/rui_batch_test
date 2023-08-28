package io.ioslab.rui.unit.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import io.ioslab.rui.common.service.ini.IniReader;
import io.ioslab.rui.common.service.mail.Mail;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailParseException;

class MailTest {

    Mail testMail;
    Resource iniTest = new ClassPathResource("config-test.ini");
    Resource iniWithEmptyMailSection = new ClassPathResource("config-empty-mail.ini");
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
    void sendMail_withEmptyMail_doesThrowMailParseException() throws IOException {
        IniReader.setIniReader(iniTest.getFile().getPath());
        assertThrows(MailParseException.class,
                     () -> testMail.sendMail("TEST_SUBJECT", "TEST_BODY",
                                             IniReader.getIniReaderder()));
    }

    @Test
    void sendMail_withEmptyMail_doesThrowNullPointerException() throws IOException {
        IniReader.setIniReader(iniWithEmptyMailSection.getFile().getPath());
        assertThrows(NullPointerException.class,
                     () -> testMail.sendMail("TEST_SUBJECT", "TEST_BODY",
                                             IniReader.getIniReaderder()));
    }


}
