package io.ioslab.rui.utils;

import java.sql.Date;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestConstants {

    public static final String CSV_FILE_NAME_DATE = "2023_07_13";
    public static final Date CSV_DATE_AS_SQL_DATE = Date.valueOf("2023-07-13");
    public static final String CSV_DATE_AS_VALID_JOB_PARAMETER = "2023-07-13";
    public static final int VALID_RECORD_PER_CSV = 100;
    public static final int FAILING_RECORD_PER_CSV = 3;
    public static final int FAILING_RECORD_PER_FAILING_CSV = 12;
    public static final String COUNT_CARICHE_SQL = "SELECT COUNT(*) FROM `RUI_CARICHE`";
    public static final String COUNT_SEDI_SQL = "SELECT COUNT(*) FROM `RUI_SEDI`";

    public static final String COUNT_MANDATI_SQL = "SELECT COUNT(*) FROM `RUI_MANDATI`";
    public static final String COUNT_COLLABORATORI_SQL = "SELECT COUNT(*) FROM `RUI_COLLABORATORI`";

    public static final String COUNT_INTERMEDIARI_SQL = "SELECT COUNT(*) FROM `RUI_INTERMEDIARI`";

    public static final String TRUNCATE_SQL = "TRUNCATE TABLE `RUI_CARICHE`;\n" +
                                              "TRUNCATE TABLE `RUI_SEDI`;\n" +
                                              "TRUNCATE TABLE `RUI_MANDATI`;\n" +
                                              "TRUNCATE TABLE `RUI_COLLABORATORI`;\n" +
                                              "TRUNCATE TABLE `RUI_INTERMEDIARI`";

    public enum DomainObjectEnumerator {
        CARICHE,
        MANDATI,
        COLLABORATORI,
        SEDI,
        INTERMEDIARI;

        @Override
        public String toString() {
            return name().toLowerCase();
        }

        public String getSqlCountStatement() {
            switch (name()) {
                case "CARICHE": {
                    return COUNT_CARICHE_SQL;
                }
                case "COLLABORATORI": {
                    return COUNT_COLLABORATORI_SQL;
                }
                case "INTERMEDIARI": {
                    return COUNT_INTERMEDIARI_SQL;
                }
                case "MANDATI" :{
                    return COUNT_MANDATI_SQL;
                }
                case "SEDI" :{
                    return COUNT_SEDI_SQL;
                }
            }
            throw new RuntimeException("NAME DOES NOT MATCH ANY");
        }
    }

}
