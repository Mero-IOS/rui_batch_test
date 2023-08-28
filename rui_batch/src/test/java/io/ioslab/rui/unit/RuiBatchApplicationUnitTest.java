package io.ioslab.rui.unit;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.ioslab.rui.batch.RuiBatchApplication;
import io.ioslab.rui.batch.utility.SendEmailError;
import io.ioslab.rui.unit.RuiBatchApplicationUnitTest.SystemExitAsExceptionSecurityManager.SystemExitException;
import java.security.Permission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

class RuiBatchApplicationUnitTest {

    @BeforeEach
    void setUp() {
        System.setSecurityManager(new SystemExitAsExceptionSecurityManager());
    }

    @Test
    void main_withEmptyArg_DoesSystemExit() {
        assertThrows(SystemExitException.class, () -> RuiBatchApplication.main(new String[]{""}));
    }



    @Test
    void main_withAnyIni_DoesRunSpringApp() {
        try (MockedStatic<SpringApplication> springApplicationMockedStatic = mockStatic(
            SpringApplication.class)) {
            String[] args = {"ANY.ini"};
            RuiBatchApplication.main(args);
            springApplicationMockedStatic.verify(() -> SpringApplication.run(RuiBatchApplication.class,args), times(1));
        }
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
