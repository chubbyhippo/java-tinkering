package org.example;

import com.google.common.collect.ImmutableList;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
class ExcelUtilsTest {


    @Mock
    private Workbook mockWorkBook;

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

    @Test
    @DisplayName("should save workbook")
    void shouldSaveWorkbook() throws IOException {
        var fileSystem = Jimfs.newFileSystem(Configuration.unix());
        var xlsxPath = fileSystem.getPath("test.xlsx");

        var toBeSavedWorkbook = new XSSFWorkbook();
        Sheet sheet = toBeSavedWorkbook.createSheet("test");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("testData");

        ExcelUtils.saveWorkbook(toBeSavedWorkbook, xlsxPath.toString());

        try (var sheets = new XSSFWorkbook(xlsxPath.toString())) {
            var savedSheet = sheets.getSheet("test");
            var savedRow = savedSheet.getRow(0);
            var savedCell = savedRow.getCell(0);

            assertThat(savedCell.getStringCellValue()).isEqualTo("testData");
        }

    }

    @Test
    @DisplayName("should close workbook")
    void shouldCloseWorkbook() throws IOException {
        ExcelUtils.closeWorkbook(mockWorkBook);
        Mockito.verify(mockWorkBook).close();
    }

}