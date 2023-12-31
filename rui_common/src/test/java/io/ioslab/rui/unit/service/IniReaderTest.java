package io.ioslab.rui.unit.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.ioslab.rui.common.model.ini.SmtpModel;
import io.ioslab.rui.unit.service.IniReaderTest.SystemExitAsExceptionSecurityManager.SystemExitException;
import io.ioslab.rui.common.model.ini.MailModel;
import io.ioslab.rui.common.model.ini.MySqlModel;
import io.ioslab.rui.common.service.ini.IniReader;
import java.io.File;
import java.io.IOException;
import java.security.Permission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class IniReaderTest {

    Resource iniTest = new ClassPathResource("config-test.ini");
    @BeforeEach
    void setUp() {
        System.setSecurityManager(new SystemExitAsExceptionSecurityManager());
    }

    @Test
    void setIniReader_EmptyString_doesSystemExit() {
        assertThrows(SystemExitException.class, () -> IniReader.setIniReader(""));
    }

    @Test
    void setIniReader_StringShorterThanFour_doesThrowIndexOutOfBoundsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> IniReader.setIniReader("ANY"));
    }
    @Test
    void setIniReader_StringNotEndingIni_doesSystemExit() {
        assertThrows(SystemExitException.class, () -> IniReader.setIniReader("ANY_MORE_THAN_FOUR"));
    }

    @Test
    void setIniReader_NotEmptyStringEndingIni_doesInitialize() {
        IniReader.setIniReader("ANY.ini");
        assertThat(IniReader.getIniReaderder()).isInstanceOf(IniReader.class);
    }

    @Test
    void getMySqlModelFromIni_NotAFile_doesThrowNullPointerException() {
        IniReader.setIniReader("ANY.ini");
        IniReader reader = IniReader.getIniReaderder();
        assertThrows(NullPointerException.class, reader::getMySqlModelFromIni);
    }

    @Test
    void getMailModelFromIni_NotAFile_doesThrowNullPointerException() {
        IniReader.setIniReader("ANY.ini");
        IniReader reader = IniReader.getIniReaderder();
        assertThrows(NullPointerException.class, reader::getMailModelFromIni);
    }

    @Test
    void getSmtpModelFromIni_NotAFile_doesThrowNullPointerException() {
        IniReader.setIniReader("ANY.ini");
        IniReader reader = IniReader.getIniReaderder();
        assertThrows(NullPointerException.class, reader::getSmtpModelFromIni);
    }

    @Test
    void getMySqlModelFromIni_EmptyIni_getsMysqlModel() throws IOException {
        IniReader.setIniReader(iniTest.getFile().getPath());
        MySqlModel mySqlModel = IniReader.getIniReaderder().getMySqlModelFromIni();
        assertThat(mySqlModel).isInstanceOf(MySqlModel.class);
    }

    @Test
    void getMailModelFromIni_EmptyIni_getsMailModel() throws IOException {
        IniReader.setIniReader(iniTest.getFile().getPath());
        MailModel mailModel = IniReader.getIniReaderder().getMailModelFromIni();
        assertThat(mailModel).isInstanceOf(MailModel.class);
    }

    @Test
    void getSmtpModelFromIni_EmptyIni_throwsNumberFormatException() throws IOException {
        String iniPath = File.createTempFile("test", ".ini").getPath();
        IniReader.setIniReader(iniPath);
        IniReader reader = IniReader.getIniReaderder();
        assertThrows(NumberFormatException.class, reader::getSmtpModelFromIni);
    }

    @Test
    void getSmtpModelFromIni_IniWithPort_getsSmtpModel() throws IOException {
        IniReader.setIniReader(iniTest.getFile().getPath());
        SmtpModel smtpModel = IniReader.getIniReaderder().getSmtpModelFromIni();
        assertThat(smtpModel).isInstanceOf(SmtpModel.class);
    }

    protected static class SystemExitAsExceptionSecurityManager extends SecurityManager {

        @Override
        public void checkPermission(Permission perm) {
        }

        @Override
        public void checkExit(int status) {
            super.checkExit(status);
            if (status != 0) {
                throw new SystemExitException(status);
            }
        }

        public static class SystemExitException extends RuntimeException {

            public SystemExitException(int exitStatus) {
                super(String.valueOf(exitStatus));
            }

        }

    }

}
