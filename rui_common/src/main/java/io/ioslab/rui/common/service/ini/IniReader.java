package io.ioslab.rui.common.service.ini;

import io.ioslab.rui.common.model.ini.MailModel;
import io.ioslab.rui.common.model.ini.MySqlModel;
import io.ioslab.rui.common.model.ini.SmtpModel;
import lombok.Getter;
import org.ini4j.Ini;

import java.io.FileReader;
import java.io.IOException;

import static io.ioslab.rui.common.utility.Costants.*;

@Getter
public class IniReader {

    private Ini reader;
    private static IniReader readerInstance;

    private IniReader(String path) {
        if (checkPath(path)) {
            try {
                reader = new Ini(new FileReader(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            System.exit(1);
    }

    private boolean checkPath(String path) {
        return checkIfPathIsEmpty(path) && checkIfPathContainFileIni(path);
    }

    private boolean checkIfPathIsEmpty(String path) {
        return !path.isEmpty();
    }

    private boolean checkIfPathContainFileIni(String path) {
        return path.substring(path.length() - 4).contains(".ini");
    }

    public static void setIniReader(String pathOfFile) {
        readerInstance = new IniReader(pathOfFile);
    }

    public static IniReader getIniReaderder() {
        return readerInstance;
    }

    public MySqlModel getMySqlModelFromIni() {
        return MySqlModel.builder()
                .url(reader.get(INI_MYSQL_SECTION, "jdbcUrl"))
                .username(reader.get(INI_MYSQL_SECTION, "username"))
                .password(reader.get(INI_MYSQL_SECTION, "password"))
                .driverClassName(reader.get(INI_MYSQL_SECTION, "driverClass"))
                .build();
    }

    public SmtpModel getSmtpModelFromIni() {
        return SmtpModel.builder()
                .host(reader.get(INI_SMTP_SECTION, "host"))
                .port(Integer.parseInt(reader.get(INI_SMTP_SECTION, "port")))
                .protocol(reader.get(INI_SMTP_SECTION, "protocol"))
                .username(reader.get(INI_SMTP_SECTION, "username"))
                .password(reader.get(INI_SMTP_SECTION, "password"))
                .build();
    }

    public MailModel getMailModelFromIni() {
        return MailModel.builder()
                .from(reader.get(INI_MAIL_SECTION, "from"))
                .to(reader.get(INI_MAIL_SECTION, "to"))
                .build();
    }
}
