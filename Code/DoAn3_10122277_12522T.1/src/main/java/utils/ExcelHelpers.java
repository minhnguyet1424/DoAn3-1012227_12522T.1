package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelHelpers
{
    private FileInputStream fis;
    private FileOutputStream fileOut;
    private Workbook wb;
    private Sheet sh;
    private Cell cell;
    private Row row;
    private CellStyle cellstyle;
    private Color mycolor;
    private String excelFilePath;
    private Map<String, Integer> columns = new HashMap<>();

    // Thiết lập file excel, sheet, và map tên cột -> chỉ số cột
    public void setExcelFile(String ExcelPath, String SheetName) throws Exception {
        try {
            File f = new File(ExcelPath);

            if (!f.exists()) {
                f.createNewFile();
                System.out.println("File doesn't exist, so created!");
            }

            fis = new FileInputStream(ExcelPath);
            wb = WorkbookFactory.create(fis);
            sh = wb.getSheet(SheetName);
            if (sh == null) {
                sh = wb.createSheet(SheetName);
            }

            this.excelFilePath = ExcelPath;

            // Đọc header dòng 0 để map cột
            Row headerRow = sh.getRow(0);
            if (headerRow != null) {
                for (Cell c : headerRow) {
                    columns.put(c.getStringCellValue(), c.getColumnIndex());
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Lấy dữ liệu ô theo số dòng và số cột
    public String getCellData(int rownum, int colnum) throws Exception{
        try{
            cell = sh.getRow(rownum).getCell(colnum);
            String CellData = null;
            switch (cell.getCellType()){
                case STRING:
                    CellData = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell))
                    {
                        CellData = String.valueOf(cell.getDateCellValue());
                    }
                    else
                    {
                        CellData = String.valueOf((long)cell.getNumericCellValue());
                    }
                    break;
                case BOOLEAN:
                    CellData = Boolean.toString(cell.getBooleanCellValue());
                    break;
                case BLANK:
                    CellData = "";
                    break;
                default:
                    CellData = "";
                    break;
            }
            return CellData;
        }catch (Exception e){
            return "";
        }
    }

    // Lấy dữ liệu ô theo tên cột và số dòng
    public String getCellData(String columnName, int rownum) throws Exception {
        return getCellData(rownum, columns.get(columnName));
    }

    // Ghi dữ liệu vào ô theo số dòng và số cột
    public void setCellData(String text, int rownum, int colnum) throws Exception {
        try{
            row  = sh.getRow(rownum);
            if(row == null) {
                row = sh.createRow(rownum);
            }
            cell = row.getCell(colnum);

            if (cell == null) {
                cell = row.createCell(colnum);
            }
            cell.setCellValue(text);

            fileOut = new FileOutputStream(excelFilePath);
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
        }catch(Exception e){
            throw (e);
        }
    }

    // Hàm tạo file excel mới, tạo sheet và ghi toàn bộ dữ liệu (danh sách mảng String)
    public void createNewExcelFileWithData(String filePath, String sheetName, List<String[]> data) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        // Tạo header và dữ liệu
        for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i);
            String[] rowData = data.get(i);
            for (int j = 0; j < rowData.length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(rowData[j]);
            }
        }

        // Tạo thư mục cha nếu chưa tồn tại
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        // Ghi file
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }

        workbook.close();
    }
}
