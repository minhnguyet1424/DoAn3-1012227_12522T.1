package test;

import base.QlWebdriver;
import config.AppURL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import pages.SearchPage;
import utils.ExcelReader;
import utils.ExcelReport;

import java.time.Duration;
import java.util.List;

public class SearchTest {
    WebDriver driver;
    List<String[]> testData;

    @BeforeClass
    public void setup() throws Exception {
        testData = ExcelReader.readSearchData("src/test/resources/DataSearch.xlsx");
        ExcelReport.startNewSearchTest("Search");
        driver = QlWebdriver.getDriver();
    }

    @AfterClass
    public void tearDown() {
        ExcelReport.saveReport();
        QlWebdriver.closeDriver();
    }

    @Test(dataProvider = "searchData")
    public void testSearchKeyword(String keyword, String expected) throws Exception {
        driver.get(AppURL.TRANG_CHU);
        SearchPage searchPage = new SearchPage(driver);
        searchPage.search(keyword);

        Thread.sleep(2000); // Có thể thay bằng WebDriverWait nếu cần

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        List<WebElement> alerts = driver.findElements(By.cssSelector(".alert.alert-warning"));

        String actual;

        if (!alerts.isEmpty()) {
            // Nếu có thông báo cảnh báo → không tìm thấy sản phẩm
            actual = cleanAlertText(alerts.get(0));
        } else {
            // Nếu không có cảnh báo → giả định tìm thấy sản phẩm
            actual = "Tìm thấy sản phẩm chứa từ khóa " + keyword;
        }

        String status = actual.equals(expected) ? "Pass" : "Fail";
        ExcelReport.writeSearchReport(keyword, expected, actual, status);
    }

    // Xử lý loại bỏ nút "× Close" khỏi nội dung thông báo
    private String cleanAlertText(WebElement alert) {
        String raw = alert.getText().trim();
        return raw.replace("×", "")
                .replace("Close", "")
                .trim();
    }

    @DataProvider(name = "searchData")
    public Object[][] searchDataProvider() {
        Object[][] data = new Object[testData.size()][2];
        for (int i = 0; i < testData.size(); ++i) {
            data[i][0] = testData.get(i)[0]; // keyword
            data[i][1] = testData.get(i)[1]; // expectedResult
        }
        return data;
    }
}
