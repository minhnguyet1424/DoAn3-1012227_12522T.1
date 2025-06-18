package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelLogger {
    private String filePath;
    private Workbook workbook;
    private Sheet sheet;

    public ExcelLogger(String filePath) throws IOException {
        this.filePath = filePath;
        File file = new File(filePath);
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            workbook = new XSSFWorkbook(fis);
            fis.close();
        } else {
            workbook = new XSSFWorkbook();
        }
    }

    public void writeRow(String sheetName, String[] headers, String[] data) throws IOException {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
        }

        int lastRowNum = sheet.getLastRowNum();
        int nextRowIndex = lastRowNum == 0 && sheet.getRow(0) == null ? 0 : lastRowNum + 1;

        // Nếu là dòng đầu tiên hoặc là dòng mới cho test mới thì thêm tiêu đề
        if (nextRowIndex == 0 || isLastTestComplete(sheet)) {
            Row testLabelRow = sheet.createRow(nextRowIndex++);
            Cell testLabelCell = testLabelRow.createCell(0);
            testLabelCell.setCellValue("Test " + getTestNumber(sheet));

            // Thêm tiêu đề
            Row headerRow = sheet.createRow(nextRowIndex++);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
        }

        // Ghi dữ liệu
        Row row = sheet.createRow(nextRowIndex);
        for (int i = 0; i < data.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(data[i]);
        }

        // Auto resize cột
        for (int i = 0; i < data.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Ghi file
        FileOutputStream fos = new FileOutputStream(filePath);
        workbook.write(fos);
        fos.close();
    }

    private boolean isLastTestComplete(Sheet sheet) {
        int lastRow = sheet.getLastRowNum();
        if (lastRow < 1) return true;

        Row row = sheet.getRow(lastRow);
        if (row == null || row.getPhysicalNumberOfCells() == 0) return true;

        return false;
    }

    private int getTestNumber(Sheet sheet) {
        int testNum = 1;
        for (Row row : sheet) {
            Cell cell = row.getCell(0);
            if (cell != null && cell.getCellType() == CellType.STRING && cell.getStringCellValue().startsWith("Test")) {
                testNum++;
            }
        }
        return testNum;
    }
}
