 package utils;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelHelpers {
    private FileInputStream fis;
    private FileOutputStream fileOut;
    private Workbook wb;
    private Sheet sh;
    private Cell cell;
    private Row row;
    private CellStyle cellstyle;
    private Color mycolor;
    private String excelFilePath;
    private Map<String, Integer> columns = new HashMap();

    public void setExcelFile(String ExcelPath, String SheetName) throws Exception {
        try {
            File f = new File(ExcelPath);
            if (!f.exists()) {
                f.createNewFile();
                System.out.println("File doesn't exist, so created!");
            }

            this.fis = new FileInputStream(ExcelPath);
            this.wb = WorkbookFactory.create(this.fis);
            this.sh = this.wb.getSheet(SheetName);
            if (this.sh == null) {
                this.sh = this.wb.createSheet(SheetName);
            }

            this.excelFilePath = ExcelPath;
            Row headerRow = this.sh.getRow(0);
            if (headerRow != null) {
                for(Cell c : headerRow) {
                    this.columns.put(c.getStringCellValue(), c.getColumnIndex());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public String getCellData(int rownum, int colnum) throws Exception {
        try {
            this.cell = this.sh.getRow(rownum).getCell(colnum);
            String CellData = null;
            switch (this.cell.getCellType()) {
                case STRING:
                    CellData = this.cell.getStringCellValue();
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(this.cell)) {
                        CellData = String.valueOf(this.cell.getDateCellValue());
                    } else {
                        CellData = String.valueOf((long)this.cell.getNumericCellValue());
                    }
                    break;
                case BOOLEAN:
                    CellData = Boolean.toString(this.cell.getBooleanCellValue());
                    break;
                case BLANK:
                    CellData = "";
                    break;
                default:
                    CellData = "";
            }

            return CellData;
        } catch (Exception var4) {
            return "";
        }
    }
    public String getCellData(String columnName, int rownum) throws Exception {
        return this.getCellData(rownum, (Integer)this.columns.get(columnName));
    }

    public void setCellData(String text, int rownum, int colnum) throws Exception {
        try {
            this.row = this.sh.getRow(rownum);
            if (this.row == null) {
                this.row = this.sh.createRow(rownum);
            }

            this.cell = this.row.getCell(colnum);
            if (this.cell == null) {
                this.cell = this.row.createCell(colnum);
            }

            this.cell.setCellValue(text);
            this.fileOut = new FileOutputStream(this.excelFilePath);
            this.wb.write(this.fileOut);
            this.fileOut.flush();
            this.fileOut.close();
        } catch (Exception e) {
            throw e;
        }
    }

    public void createNewExcelFileWithData(String filePath, String sheetName, List<String[]> data) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        for(int i = 0; i < data.size(); ++i) {
            Row row = sheet.createRow(i);
            String[] rowData = (String[])data.get(i);

            for(int j = 0; j < rowData.length; ++j) {
                Cell cell = row.createCell(j);
                cell.setCellValue(rowData[j]);
            }
        }

        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }

        workbook.close();
    }
}