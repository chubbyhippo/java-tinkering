package org.example;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
class ExcelUtilsTest {

    @Test
    @DisplayName("should throw illegal state exception when initialized")
    void shouldThrowIllegalStateExceptionWhenInitialized() throws NoSuchMethodException {
        var constructor = ExcelUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        assertThatExceptionOfType(InvocationTargetException.class)
                .isThrownBy(constructor::newInstance)
                .withCauseInstanceOf(IllegalAccessException.class);
    }

    @Test
    @DisplayName("should create workbook as an instance of XSSFWorkbook")
    void shouldCreateWorkbookAsAnInstanceOfXssfWorkbook() {
        var wb = ExcelUtils.createWorkbook();
        assertThat(wb).isInstanceOf(XSSFWorkbook.class);
    }

}