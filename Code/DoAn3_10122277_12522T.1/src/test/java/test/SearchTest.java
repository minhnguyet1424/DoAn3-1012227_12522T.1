package test;

import base.BaseSetup;
import config.AppURL;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import pages.SearchPage;
import utils.ExcelReader;
import utils.ExcelReport;

import java.time.Duration;
import java.util.List;

public class SearchTest extends BaseSetup {
    List<String[]> testData;

    @BeforeClass
    public void setup() throws Exception {
        initializeDriver();
        testData = ExcelReader.readSearchData("src/test/resources/InputData.xlsx");
        ExcelReport.startNewSearchTest("Search");
    }
    @AfterClass
    public void tearDown() {
        tearDownAfterTest();
    }
    @Test(dataProvider = "searchData")
    public void testSearchKeyword(String keyword, String expected) throws Exception {
        driver.get(AppURL.TRANG_CHU);
        SearchPage searchPage = new SearchPage(driver);
        searchPage.search(keyword);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        String actual;

        try {
            WebElement alert = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector(".alert.alert-warning")
            ));
            actual = cleanAlertText(alert);
        } catch (Exception e) {
            actual = "Tìm thấy";
        }

        String status = actual.equals(expected) ? "Pass" : "Fail";
        ExcelReport.writeSearchReport(keyword, expected, actual, status);
    }

    private String cleanAlertText(WebElement alert) {
        String raw = alert.getText().trim();
        return raw.replace("×", "").replace("Close", "").trim();
    }

    @DataProvider(name = "searchData")
    public Object[][] searchDataProvider() {
        Object[][] data = new Object[testData.size()][2];
        for (int i = 0; i < testData.size(); ++i) {
            data[i][0] = testData.get(i)[0]; // keyword
            data[i][1] = testData.get(i)[1]; // expected result
        }
        return data;
    }



}
