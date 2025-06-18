package utils;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

    // Số cột cho từng loại dữ liệu
    private static final int LOGIN_COLS = 3;
    private static final int SEARCH_COLS = 2;
    private static final int SIGNUP_COLS = 5;
    private static final int ORDER_COLS = 13;

    // Hàm dùng chung: đọc file Excel và trả về danh sách dòng với số cột cố định
    private static List<String[]> readExcel(String filePath, int expectedColumns) {
        List<String[]> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String[] rowData = new String[expectedColumns];
                boolean isEmptyRow = true;

                for (int j = 0; j < expectedColumns; j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData[j] = cell.toString().trim();
                    if (!rowData[j].isEmpty()) {
                        isEmptyRow = false;
                    }
                }

                if (!isEmptyRow) {
                    data.add(rowData);
                }
            }
        } catch (Exception e) {
            System.err.println(" Lỗi khi đọc file Excel: " + e.getMessage());
            e.printStackTrace();
        }

        return data;
    }

    // Đăng nhập: Email | Password | ExpectedError
    public static List<String[]> readData(String filePath) {
        List<String[]> raw = readExcel(filePath, LOGIN_COLS);
        List<String[]> result = new ArrayList<>();
        for (int i = 0; i < raw.size(); i++) {
            String[] row = raw.get(i);
            result.add(new String[]{row[0], row[1], row[2], String.valueOf(i + 1)});
        }
        return result;
    }

    // Tìm kiếm: Keyword | ExpectedText
    public static List<String[]> readSearchData(String filePath) {
        return readExcel(filePath, SEARCH_COLS);
    }

//Đăng ký
    public static List<String[]> readSignupData(String filePath) {

        List<String[]> raw = readExcel(filePath, SIGNUP_COLS);
        List<String[]> result = new ArrayList<>();
        for (String[] row : raw) {
            if (row.length >= 5) {
                result.add(new String[]{row[0], row[1], row[2], row[3], row[4]});
            }
        }
        return result;
    }
//Đặt hàng

    public static List<String[]> readOrderData(String filePath) {
        List<String[]> raw = readExcel(filePath, ORDER_COLS);
        List<String[]> result = new ArrayList<>();
        for (String[] row : raw) {
            if (row.length >= 13) {
                result.add(new String[]{
                        row[0],  // Tên sách
                        row[1],  //Số lg
                        row[2],  // Email
                        row[3],  // Họ tên
                        row[4],  // Phone
                        row[5],  // Địa chỉ
                        row[6],  // Tỉnh
                        row[7],  // Quận
                        row[8],  // Xã
                        row[9],  // Ghi chú
                        row[10],  // Mã giảm giá
                        row[11], // PTTT
                        row[12]  // Kết quả mong đợi
                });
            }
        }
        return result;
    }



}

