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
    private static int stt = 1;


    public static void startNewLoginTest(String sheetName) {
        resetSTT();
        currentSheetName = sheetName;
        setupSheetWithHeaders(new String[]{
                "STT", "Thời gian", "Email", "Password", "Kết quả mong đợi", "Kết quả thực tế", "Trạng thái"
        });
    }

    public static void writeLoginReport(String email, String password, String expected, String actual, String status) {
        Row row = sheet.createRow(currentRow++);
        row.createCell(0).setCellValue(stt++);
        row.createCell(1).setCellValue(getCurrentTime());
        row.createCell(2).setCellValue(email);
        row.createCell(3).setCellValue(password);
        row.createCell(4).setCellValue(expected);
        row.createCell(5).setCellValue(actual);
        row.createCell(6).setCellValue(status);
    }

    public static void startNewSignupTest(String sheetName) {
        resetSTT();
        currentSheetName = sheetName;
        setupSheetWithHeaders(new String[]{
                "STT", "Thời gian", "Họ", "Tên", "Email", "Mật khẩu", "Kết quả mong đợi", "Kết quả thực tế", "Trạng thái"
        });
    }

    public static void writeSignupReport(String lastName, String firstName, String email, String password,
                                         String expected, String actual, String status) {
        Row row = sheet.createRow(currentRow++);
        row.createCell(0).setCellValue(stt++);
        row.createCell(1).setCellValue(getCurrentTime());
        row.createCell(2).setCellValue(lastName);
        row.createCell(3).setCellValue(firstName);
        row.createCell(4).setCellValue(email);
        row.createCell(5).setCellValue(password);
        row.createCell(6).setCellValue(expected);
        row.createCell(7).setCellValue(actual);
        row.createCell(8).setCellValue(status);
    }

    public static void startNewSearchTest(String sheetName) {
        resetSTT();
        currentSheetName = sheetName;
        setupSheetWithHeaders(new String[]{
                "STT", "Thời gian", "Từ khóa", "Kết quả mong đợi", "Kết quả thực tế", "Trạng thái"
        });
    }

    public static void writeSearchReport(String keyword, String expected, String actual, String status) {
        Row row = sheet.createRow(currentRow++);
        row.createCell(0).setCellValue(stt++);
        row.createCell(1).setCellValue(getCurrentTime());
        row.createCell(2).setCellValue(keyword);
        row.createCell(3).setCellValue(expected);
        row.createCell(4).setCellValue(actual);
        row.createCell(5).setCellValue(status);
    }

    public static void startNewOrderTest(String sheetName) {
        resetSTT();
        currentSheetName = sheetName;
        setupSheetWithHeaders(new String[]{
                "STT", "Thời gian", "Tên sách", "Họ tên", "SĐT", "Địa chỉ", "Tỉnh", "Huyện", "Xã",
                "Ghi chú", "Mã giảm giá", "PTTT", "Kết quả mong đợi", "Kết quả thực tế", "Trạng thái"
        });
    }

    public static void writeOrderReport(
            String bookTitle, String email, String fullName, String phone, String address, String province,
            String district, String ward, String note, String coupon, String payment,
            String expected, String actual, String status
    )
    {
        Row row = sheet.createRow(currentRow++);
        int col = 0;
        row.createCell(0).setCellValue(bookTitle);
        row.createCell(1).setCellValue(email);
        row.createCell(2).setCellValue(fullName);
        row.createCell(3).setCellValue(phone);
        row.createCell(4).setCellValue(address);
        row.createCell(5).setCellValue(province);
        row.createCell(6).setCellValue(district);
        row.createCell(7).setCellValue(ward);
        row.createCell(8).setCellValue(note);
        row.createCell(9).setCellValue(coupon);
        row.createCell(10).setCellValue(payment);
        row.createCell(11).setCellValue(expected);
        row.createCell(12).setCellValue(actual);
        row.createCell(13).setCellValue(status);

    }

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
        titleCell.setCellValue("Test " + testCount + " - " + getCurrentTime());
    }

    public static void saveReport() {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            for (int i = 0; i < sheet.getRow(1).getLastCellNum(); i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(fos);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    private static void resetSTT() {
        stt = 1;
    }
}
