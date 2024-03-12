package org.example;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ExcelUtils {
    private ExcelUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Workbook createWorkbook() {
        return new XSSFWorkbook();
    }

    public static void saveWorkbook(Workbook workbook, String filepath) throws IOException {
        try (var newOutputStream = Files.newOutputStream(Path.of(filepath))) {
            workbook.write(newOutputStream);
        }
    }

    public static Workbook getWorkbook(String filepath) throws IOException {
        try (var fis = Files.newInputStream(Path.of(filepath))) {
            return new XSSFWorkbook(fis);
        }
    }

    public static void closeWorkbook(Workbook workbook) throws IOException {
        workbook.close();
    }

    public static Sheet getSheet(Workbook workbook, String sheetName) {
        return workbook.getSheet(sheetName);
    }

    public static String getCellValue(Sheet sheet, int rowNum, int colNum) {
        var row = sheet.getRow(rowNum);
        if (row != null) {
            var cell = row.getCell(colNum);
            if (cell != null) {
                return cell.getStringCellValue();
            }
        }
        return "";
    }

    public static void setCellValue(Sheet sheet, int rowNum, int colNum, String value) {
        var row = sheet.getRow(rowNum);
        if (row == null) {
            row = sheet.createRow(rowNum);
        }

        var cell = row.getCell(colNum);
        if (cell == null) {
            cell = row.createCell(colNum);
        }
        cell.setCellValue(value);
    }

}