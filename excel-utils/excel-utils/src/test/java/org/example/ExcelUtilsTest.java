package org.example;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

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
        var sheet = toBeSavedWorkbook.createSheet("test");
        var row = sheet.createRow(0);
        var cell = row.createCell(0);
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
    @DisplayName("should get workbook")
    void shouldGetWorkbook() throws IOException {
        var fileSystem = Jimfs.newFileSystem(Configuration.unix());
        var xlsxPath = fileSystem.getPath("test.xlsx");

        try (var toBeSavedWorkbook = new XSSFWorkbook()) {
            var sheet = toBeSavedWorkbook.createSheet("test");
            var row = sheet.createRow(0);
            var cell = row.createCell(0);
            cell.setCellValue("testData");

            try (var fos = new FileOutputStream(xlsxPath.toString())) {
                toBeSavedWorkbook.write(fos);
            }

            var savedWorkbook = ExcelUtils.getWorkbook(xlsxPath.toString());

            var savedStringValue = savedWorkbook.getSheet("test")
                    .getRow(0)
                    .getCell(0)
                    .getStringCellValue();

            assertThat(savedStringValue).isEqualTo("testData");
        }
    }


    @Test
    @DisplayName("should get cell value")
    void shouldGetCellValue() throws IOException {
        try (var workbook = new XSSFWorkbook()) {
            var sheet = workbook.createSheet("test");
            var row = sheet.createRow(0);
            var cell = row.createCell(0);
            cell.setCellValue("testData");

            var cellValue = ExcelUtils.getCellValue(sheet, 0, 0);
            assertThat(cellValue).isEqualTo("testData");
        }

    }

    @Test
    @DisplayName("should set cell value")
    void shouldSetCellValue() throws IOException {

        try (var workbook = new XSSFWorkbook()) {
            var sheet = workbook.createSheet("test");
            ExcelUtils.setCellValue(sheet, 0, 0, "testdata");

            var stringCellValue = sheet.getRow(0)
                    .getCell(0)
                    .getStringCellValue();

            assertThat(stringCellValue).isEqualTo("testdata");
        }
    }

    @Test
    @DisplayName("should close workbook")
    void shouldCloseWorkbook() throws IOException {
        ExcelUtils.closeWorkbook(mockWorkBook);
        Mockito.verify(mockWorkBook).close();
    }

    @Test
    @DisplayName("should get sheet")
    void shouldGetSheet() throws IOException {
        try (var workbook = new XSSFWorkbook()) {
            var sheet = workbook.createSheet("test");
            var sheetFromExcelUtils = ExcelUtils.getSheet(workbook, "test");

            assertThat(sheetFromExcelUtils).isEqualTo(sheet);
        }
    }

}