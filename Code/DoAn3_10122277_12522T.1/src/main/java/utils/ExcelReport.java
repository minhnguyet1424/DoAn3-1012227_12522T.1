package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelReport {
    private XSSFWorkbook workbook;
    private Sheet sheet;
    private String filePath;
    private int currentRow = 1;

    public ExcelReport(String filePath, List<String> headers) {
        this.filePath = filePath;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Test Report");

        // Ghi header
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            headerRow.createCell(i).setCellValue(headers.get(i));
        }
    }

    // Ghi kết quả cho search test
    public void writeSearchTestResult(int stt, String keyword, String expected, String actual) {
        Row row = sheet.createRow(currentRow++);
        row.createCell(0).setCellValue(stt);
        row.createCell(1).setCellValue(keyword);
        row.createCell(2).setCellValue(expected);
        row.createCell(3).setCellValue(actual);
    }
    // Ghi kết quả cho search test
    public void writeSignupTestResult(int stt, String lastName, String firstName, String email, String password, String expected, String actual, String passes) {
        Row row = sheet.createRow(currentRow++);
        row.createCell(0).setCellValue(stt);
        row.createCell(1).setCellValue(lastName);
        row.createCell(2).setCellValue(firstName);
        row.createCell(3).setCellValue(email);
        row.createCell(4).setCellValue(password);
        row.createCell(5).setCellValue(expected);
        row.createCell(6).setCellValue(actual);
        row.createCell(7).setCellValue(passes);
    }

    public void close() throws IOException {
        FileOutputStream outputStream = new FileOutputStream(filePath);

        workbook.write(outputStream);
        outputStream.flush();  // Đảm bảo đẩy hết dữ liệu xuống file
        outputStream.close();  // Đóng stream
        workbook.close();  // Đóng workbook
    }
}
