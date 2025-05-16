package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {
    public static List<String[]> readData(String filePath) {
        List<String[]> data = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                String email = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
                String password = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
                String expected = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
                data.add(new String[]{email, password, expected, String.valueOf(i)}); // thêm index dòng
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
