package test;

import base.BaseSetup;
import base.QlWebdriver;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import pages.SearchPage;
import utils.ExcelReader;
import utils.ExcelReport;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

public class SearchTest extends BaseSetup {
    WebDriver driver;
    SearchPage searchPage;
    ExcelReport report;
    String inputPath = "src/test/resources/DataSearch.xlsx";
    String reportPath = "test-output/SearchTestReport.xlsx";

    @BeforeClass
    public void setup() throws Exception {
        // Xóa file cũ nếu có
        File reportFile = new File(reportPath);
        if (reportFile.exists()) {
            reportFile.delete();
        }

        driver = setupDriver();
        driver.get("https://sachtaodan.vn/");
        searchPage = new SearchPage(driver);

        // Tạo header riêng cho báo cáo Search
        List<String> searchHeaders = Arrays.asList("STT", "Từ khóa", "Kết quả mong đợi", "Kết quả thực tế");
        report = new ExcelReport(reportPath, searchHeaders);
    }

    @DataProvider(name = "searchData")
    public Object[][] getSearchData() {
        List<String[]> data = ExcelReader.readSearchData(inputPath);
        Object[][] result = new Object[data.size()][3];
        for (int i = 0; i < data.size(); i++) {
            result[i] = data.get(i);
        }
        return result;
    }

    @Test(dataProvider = "searchData")
    public void testSearch(String keyword, String expectedText, String rowIndexStr) throws IOException {
        int rowIndex = Integer.parseInt(rowIndexStr);

        searchPage.searchKeyword(keyword);
        try {
            Thread.sleep(2000); // chờ load kết quả
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String actualText = searchPage.getSearchResultText();

        // Ghi vào báo cáo Search
        report.writeSearchTestResult(rowIndex, keyword, expectedText, actualText);
    }

    @AfterClass
    public void tearDown() throws IOException {
        report.close();
        QlWebdriver.closeDriver();
    }
}
