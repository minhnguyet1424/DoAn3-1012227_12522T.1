package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelReport {
    private static Workbook workbook;
    private static Sheet sheet;
    private static String filePath = "test-output/report.xlsx";
    private static String currentSheetName = "";
    private static int currentRow = 0;
    private static int testCount = 1;

    // === BẮT ĐẦU LOGIN ===
    public static void startNewLoginTest(String sheetName) {
        currentSheetName = sheetName;
        setupSheetWithHeaders(new String[]{
                "STT", "Thời gian", "Email", "Password", "Kết quả mong đợi", "Kết quả thực tế", "Trạng thái"
        });
    }

    public static void writeLoginReport(String email, String password, String expected, String actual, String status) {
        Row row = sheet.createRow(currentRow++);
        int index = getTestCaseIndexInCurrentTest();
        row.createCell(0).setCellValue(index);
        row.createCell(1).setCellValue(getCurrentTime());
        row.createCell(2).setCellValue(email);
        row.createCell(3).setCellValue(password);
        row.createCell(4).setCellValue(expected);
        row.createCell(5).setCellValue(actual);
        row.createCell(6).setCellValue(status);
    }
    // === KẾT THÚC LOGIN ===

    // === BẮT ĐẦU SIGNUP ===
    public static void startNewSignupTest(String sheetName) {
        currentSheetName = sheetName;
        setupSheetWithHeaders(new String[]{
                "STT", "Thời gian", "Họ", "Tên", "Email", "Mật khẩu", "Kết quả mong đợi", "Kết quả thực tế", "Trạng thái"
        });
    }

    public static void writeSignupReport(String lastName, String firstName, String email, String password,
                                         String expected, String actual, String status) {
        Row row = sheet.createRow(currentRow++);
        int index = getTestCaseIndexInCurrentTest();
        row.createCell(0).setCellValue(index);
        row.createCell(1).setCellValue(getCurrentTime());
        row.createCell(2).setCellValue(lastName);
        row.createCell(3).setCellValue(firstName);
        row.createCell(4).setCellValue(email);
        row.createCell(5).setCellValue(password);
        row.createCell(6).setCellValue(expected);
        row.createCell(7).setCellValue(actual);
        row.createCell(8).setCellValue(status);
    }
    // === KẾT THÚC SIGNUP ===

    // === BẮT ĐẦU SEARCH ===
    public static void startNewSearchTest(String sheetName) {
        currentSheetName = sheetName;
        setupSheetWithHeaders(new String[]{
                "STT", "Thời gian", "Từ khóa", "Kết quả mong đợi", "Kết quả thực tế", "Trạng thái"
        });
    }

    public static void writeSearchReport(String keyword, String expected, String actual, String status) {
        Row row = sheet.createRow(currentRow++);
        int index = getTestCaseIndexInCurrentTest();
        row.createCell(0).setCellValue(index);
        row.createCell(1).setCellValue(getCurrentTime());
        row.createCell(2).setCellValue(keyword);
        row.createCell(3).setCellValue(expected);
        row.createCell(4).setCellValue(actual);
        row.createCell(5).setCellValue(status);
    }
    // === KẾT THÚC SEARCH ===
    // === BẮT ĐẦU ORDER ===
    public static void startNewOrderTest(String sheetName) {
        currentSheetName = sheetName;
        setupSheetWithHeaders(new String[]{
                "STT", "Thời gian", "Tên sách", "Họ tên", "SĐT", "Địa chỉ", "Tỉnh", "Huyện", "Xã",
                "Ghi chú", "Mã giảm giá", "PTTT", "Kết quả mong đợi", "Kết quả thực tế", "Trạng thái"
        });
    }

    public static void writeOrderReport(String bookTitle, String fullName, String phone, String address, String province,
                                        String district, String ward, String note, String coupon, String payment,
                                        String expected, String actual, String status) {
        Row row = sheet.createRow(currentRow++);
        int col = 0;
        row.createCell(col++).setCellValue(getTestCaseIndexInCurrentTest());
        row.createCell(col++).setCellValue(getCurrentTime());
        row.createCell(col++).setCellValue(bookTitle);
        row.createCell(col++).setCellValue(fullName);
        row.createCell(col++).setCellValue(phone);
        row.createCell(col++).setCellValue(address);
        row.createCell(col++).setCellValue(province);
        row.createCell(col++).setCellValue(district);
        row.createCell(col++).setCellValue(ward);
        row.createCell(col++).setCellValue(note);
        row.createCell(col++).setCellValue(coupon);
        row.createCell(col++).setCellValue(payment);
        row.createCell(col++).setCellValue(expected);
        row.createCell(col++).setCellValue(actual);
        row.createCell(col).setCellValue(status);
    }
    // === KẾT THÚC ORDER ===
    // === HÀM DÙNG CHUNG ===
    private static void setupSheetWithHeaders(String[] headers) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                workbook = new XSSFWorkbook(fis);
                Sheet existingSheet = workbook.getSheet(currentSheetName);
                if (existingSheet != null) {
                    sheet = existingSheet;
                    currentRow = sheet.getLastRowNum() + 2;
                } else {
                    sheet = workbook.createSheet(currentSheetName);
                    currentRow = 0;
                }
            } else {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet(currentSheetName);
                currentRow = 0;
            }

            testCount = countPreviousTests();
            writeTestTitle();

            Row headerRow = sheet.createRow(currentRow++);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//// === BẮT ĐẦU ADD ADDRESS ===
//    public static void startNewAddAddressTest(String sheetName) {
//      currentSheetName = sheetName;
//     setupSheetWithHeaders(new String[]{
//             "STT", "Thời gian", "Email", "Họ tên", "SĐT", "Địa chỉ", "Tỉnh", "Huyện", "Xã",
//             "Ghi chú", "Mã giảm giá", "PTTT", "Kết quả mong đợi", "Kết quả thực tế", "Trạng thái"
//     });
//    }
//
//    public static void writeOrderReport(String email, String fullName, String phone, String address, String province,
//                                        String district, String ward, String note, String coupon, String payment,
//                                        String expected, String actual, String status) {
//        Row row = sheet.createRow(currentRow++);
//        int col = 0;
//        row.createCell(col++).setCellValue(getTestCaseIndexInCurrentTest()); // STT
//        row.createCell(col++).setCellValue(getCurrentTime());                // Thời gian
//        row.createCell(col++).setCellValue(email);                           // Email
//        row.createCell(col++).setCellValue(fullName);                        // Họ tên
//        row.createCell(col++).setCellValue(phone);                           // SĐT
//        row.createCell(col++).setCellValue(address);                         // Địa chỉ
//        row.createCell(col++).setCellValue(province);                        // Tỉnh
//        row.createCell(col++).setCellValue(district);                        // Huyện
//        row.createCell(col++).setCellValue(ward);                            // Xã
//        row.createCell(col++).setCellValue(note);                            // Ghi chú
//        row.createCell(col++).setCellValue(coupon);                          // Mã giảm giá
//        row.createCell(col++).setCellValue(payment);                         // PTTT
//        row.createCell(col++).setCellValue(expected);                        // Kết quả mong đợi
//        row.createCell(col++).setCellValue(actual);                          // Kết quả thực tế
//        row.createCell(col).setCellValue(status);                            // Trạng thái
//    }


    private static int countPreviousTests() {
        int count = 1;
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null && row.getCell(0) != null) {
                String value = row.getCell(0).toString();
                if (value.startsWith("Test ")) {
                    count++;
                }
            }
        }
        return count;
    }

    private static void writeTestTitle() {
        Row testTitleRow = sheet.createRow(currentRow++);
        Cell titleCell = testTitleRow.createCell(0);
        titleCell.setCellValue("Test " + testCount);
    }

    private static int getTestCaseIndexInCurrentTest() {
        int count = 0;
        for (int i = currentRow - 1; i >= 0; i--) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            Cell cell = row.getCell(0);
            if (cell == null) continue;
            String value = cell.toString();
            if (value.startsWith("Test ")) {
                break;
            } else {
                count++;
            }
        }
        return count + 1;
    }

    public static void saveReport() {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
