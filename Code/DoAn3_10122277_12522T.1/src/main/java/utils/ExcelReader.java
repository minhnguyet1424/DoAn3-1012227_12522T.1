package utils;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {
    public static List<String[]> readData(String filePath) {
        List<String[]> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); ++i) {
                Row row = sheet.getRow(i);
                String email = row.getCell(0, MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
                String password = row.getCell(1, MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
                String expected = row.getCell(2, MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
                data.add(new String[]{email, password, expected, String.valueOf(i)});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    // Hàm dành riêng cho đọc dữ liệu tìm kiếm (cột: keyword - expectedText)
    public static List<String[]> readSearchData(String filePath) {
        List<String[]> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); ++i) {
                Row row = sheet.getRow(i);
                String keyword = row.getCell(0, MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
                String expected = row.getCell(1, MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
                data.add(new String[]{keyword, expected, String.valueOf(i)});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    public static List<String[]> readSignupData(String filePath) {
        List<String[]> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); ++i) {
                Row row = sheet.getRow(i);
                String lastName = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
                String firstName = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
                String email = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
                String password = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
                String expected = row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
                data.add(new String[]{lastName, firstName, email, password, expected});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    public static List<String[]> readOrderData(String filePath) {
        List<String[]> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); ++i) {
                Row row = sheet.getRow(i);
                String lastName = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
                String firstName = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
                String email = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
                String password = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
                String expected = row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
                data.add(new String[]{lastName, firstName, email, password, expected});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

}