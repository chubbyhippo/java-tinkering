package io.github.chubbyhippo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;

class SftpUtilsTest {
    @Test
    @DisplayName("should throw illegal state exception when initialized")
    void shouldThrowIllegalStateExceptionWhenInitialized() {
        var constructor = SftpUtils.class.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
        } catch (IllegalStateException | InstantiationException | IllegalAccessException |
                 InvocationTargetException exception) {
            assertThat(exception.getCause().getClass()).isEqualTo(IllegalStateException.class);
            assertThat(exception.getCause().getMessage()).isEqualTo("Utility class");
        }
    }

}