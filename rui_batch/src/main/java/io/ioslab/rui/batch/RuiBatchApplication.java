package io.ioslab.rui.batch;

import io.ioslab.rui.common.service.ini.IniReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RuiBatchApplication {

    public static void main(String[] args) {
        if (args[0].contains(".ini"))
            IniReader.setIniReader(args[0]);
        else
            System.exit(1);

        SpringApplication.run(RuiBatchApplication.class, args);
    }
}
