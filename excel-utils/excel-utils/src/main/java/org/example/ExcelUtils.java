package org.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUtils {

    private ExcelUtils() throws IllegalAccessException {
        throw new IllegalAccessException("Utility Class");
    }

    public static Workbook getWorkbook(String filepath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filepath)) {
            return new XSSFWorkbook(fis);
        }
    }

    public static void closeWorkbook(Workbook workbook) throws IOException {
        workbook.close();
    }

    public static Sheet getSheet(Workbook workbook, String sheetName) {
        return workbook.getSheet(sheetName);
    }

    public static void setCellValue(Sheet sheet, int rowNum, int colNum, String value) {
        Row row = sheet.getRow(rowNum);
        if (row == null) {
            row = sheet.createRow(rowNum);
        }

        Cell cell = row.getCell(colNum);
        if (cell == null) {
            cell = row.createCell(colNum);
        }

        cell.setCellValue(value);
    }

    public static void saveWorkbook(Workbook workbook, String filepath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filepath)) {
            workbook.write(fos);
        }
    }
}