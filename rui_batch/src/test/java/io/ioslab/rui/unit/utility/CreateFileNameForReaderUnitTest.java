package io.ioslab.rui.unit.utility;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.ioslab.rui.batch.utility.CreateFileNameForReader;
import org.junit.jupiter.api.Test;

class CreateFileNameForReaderUnitTest {

    @Test
    void createFileName_withEmptyStrings_returnStringContainsPath(){
        assertThat(CreateFileNameForReader.createFileName("test_path","","")).contains("test_path");
    }

    @Test
    void createFileName_withEmptyStrings_returnStringContainsDateWithDashAsUnderscore(){
        assertThat(CreateFileNameForReader.createFileName("","","date-")).contains("date_");
    }

}
