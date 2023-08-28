package io.ioslab.rui.unit.utility;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.ioslab.rui.batch.utility.CreateFileNameForReader;
import org.junit.jupiter.api.Test;

class CreateFileNameForReaderUnitTest {

    @Test
    void createFileName_withEmptyFileName_returnStringContainsPath() {
        assertThat(CreateFileNameForReader.createFileName("test_path", "", "")).contains(
            "test_path");
    }

    @Test
    void createFileName_withEmptyStrings_returnStringContainsDateWithDashAsUnderscore() {
        assertThat(CreateFileNameForReader.createFileName("", "", "date-")).contains("date_");
    }

    @Test
    void createFileName_withNullFileName_returnStringContainsPath() {
        assertThat(CreateFileNameForReader.createFileName("test_path", null, "")).contains(
            "test_path");
    }

    @Test
    void createFileName_withNullPath_doesThrowNullPointerException() {
        assertThrows(NullPointerException.class,
                     () -> CreateFileNameForReader.createFileName(null, "", ""));
    }

}
