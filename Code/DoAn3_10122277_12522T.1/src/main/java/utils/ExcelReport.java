package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;

public class ExcelReport {

    private Workbook workbook;
    private Sheet sheet;
    private String filePath;

    public ExcelReport(String filePath) throws IOException {
        this.filePath = filePath;
        File file = new File(filePath);

        if (file.exists()) {
            // Nếu file đã có, mở ra để ghi tiếp
            try (FileInputStream fis = new FileInputStream(file)) {
                workbook = new XSSFWorkbook(fis);
                sheet = workbook.getSheetAt(0);
            }
        } else {
            // Nếu chưa có thì tạo mới
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("TestReport");
            createHeader();
            saveFile(); // lưu file mới tạo
        }
    }

    private void createHeader() {
        Row header = sheet.createRow(0);
        String[] headers = {"STT", "Email", "Password", "Kết quả mong đợi", "Kết quả thực tế", "Trạng thái"};
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
        }
    }

    public void writeTestResult(int stt, String email, String password, String expected, String actual) throws IOException {
        int rowCount = sheet.getLastRowNum();
        Row row = sheet.createRow(rowCount + 1);

        row.createCell(0).setCellValue(stt);
        row.createCell(1).setCellValue(email);
        row.createCell(2).setCellValue(password);
        row.createCell(3).setCellValue(expected);
        row.createCell(4).setCellValue(actual);

        String status = actual.equalsIgnoreCase(expected) ? "Pass" : "Fail";
        row.createCell(5).setCellValue(status);

        // Tự động resize các cột để vừa nội dung
        for (int i = 0; i <= 5; i++) {
            sheet.autoSizeColumn(i);
        }

        saveFile();
    }

    private void saveFile() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
    }

    public void close() throws IOException {
        workbook.close();
    }
}
