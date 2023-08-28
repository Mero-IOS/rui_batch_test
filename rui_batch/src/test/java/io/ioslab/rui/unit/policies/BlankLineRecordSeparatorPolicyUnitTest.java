package io.ioslab.rui.unit.policies;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.ioslab.rui.batch.policies.BlankLineRecordSeparatorPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BlankLineRecordSeparatorPolicyUnitTest {

    BlankLineRecordSeparatorPolicy blankLineRecordSeparatorPolicy;

    @BeforeEach
    void setBlankLineRecordSeparatorPolicy() {
        blankLineRecordSeparatorPolicy = new BlankLineRecordSeparatorPolicy();
    }

    @Test
    void isEndOfRecord_anyNonEmptyAfterTrim_doesReturnTrue() {
        assertThat(blankLineRecordSeparatorPolicy.isEndOfRecord("ANY_STRING")).isTrue();
    }

    @Test
    void isEndOfRecord_anyEmptyAfterTrim_doesReturnFalse() {
        assertThat(blankLineRecordSeparatorPolicy.isEndOfRecord("    ")).isFalse();
    }

    @Test
    void isEndOfRecord_doubleReturn_doesReturnFalse() {
        assertThat(blankLineRecordSeparatorPolicy.isEndOfRecord("\n\n    ")).isFalse();
    }

    @Test
    void postProcess_anyEmptyAfterTrim_doesReturnNull() {
        assertThat(blankLineRecordSeparatorPolicy.postProcess("    ")).isNull();
    }

    @Test
    void postProcess_doubleReturn_doesReturnNull() {
        assertThat(blankLineRecordSeparatorPolicy.postProcess("\n\n    ")).isNull();
    }

    @Test
    void postProcess_anyNonEmptyAfterTrim_doesReturnAnyString() {
        assertThat(blankLineRecordSeparatorPolicy.postProcess("ANY_STRING")).isEqualTo(
            "ANY_STRING");
    }

}
