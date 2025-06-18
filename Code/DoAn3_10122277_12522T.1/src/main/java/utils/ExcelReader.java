package utils;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.*;

public class ExcelReader {

    // Số cột cho từng loại dữ liệu
    private static final int LOGIN_COLS = 3;
    private static final int SEARCH_COLS = 2;
    private static final int SIGNUP_COLS = 5;
    private static final int ORDER_COLS = 13;

    // ✅ Hàm đọc sheet theo tên
    private static List<String[]> readExcel(String filePath, String sheetName, int expectedColumns) {
        List<String[]> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                System.err.println("Không tìm thấy sheet: " + sheetName);
                return data;
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String[] rowData = new String[expectedColumns];
                boolean isEmptyRow = true;

                for (int j = 0; j < expectedColumns; j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cell.setCellType(CellType.STRING);
                    rowData[j] = cell.getStringCellValue().trim();
                    if (!rowData[j].isEmpty()) {
                        isEmptyRow = false;
                    }
                }

                if (!isEmptyRow) {
                    data.add(rowData);
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi đọc file Excel: " + e.getMessage());
            e.printStackTrace();
        }

        return data;
    }

    // Đăng nhập: Email | Password | ExpectedError
    public static List<String[]> readLoginData(String filePath) {
        List<String[]> raw = readExcel(filePath, "Login", LOGIN_COLS);
        List<String[]> result = new ArrayList<>();
        for (int i = 0; i < raw.size(); i++) {
            String[] row = raw.get(i);
            result.add(new String[]{row[0], row[1], row[2], String.valueOf(i + 1)});
        }
        return result;
    }

    // Tìm kiếm: Keyword | ExpectedText
    public static List<String[]> readSearchData(String filePath) {
        return readExcel(filePath, "Search", SEARCH_COLS);
    }

    // Đăng ký: LastName | FirstName | Email | Password | Expected
    public static List<String[]> readSignupData(String filePath) {
        return readExcel(filePath, "Signup", SIGNUP_COLS);
    }

    // Đặt hàng: 13 cột
    public static List<String[]> readOrderData(String filePath) {
        return readExcel(filePath, "Order", ORDER_COLS);
    }
}
