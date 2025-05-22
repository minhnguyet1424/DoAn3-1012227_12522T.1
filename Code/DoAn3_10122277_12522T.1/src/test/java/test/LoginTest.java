 package test;

import base.QlWebdriver;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.ExcelHelpers;
import utils.ExcelReader;

public class LoginTest {
    WebDriver driver;
    String excelPath = "src/test/resources/Book1.xlsx";
    String reportPath = "test-output/LoginTestReport.xlsx";
    ExcelHelpers excelHelpers = new ExcelHelpers();
    List<String[]> testData;

    @BeforeClass
    public void setup() throws Exception {
        this.testData = ExcelReader.readData(this.excelPath);
        this.excelHelpers.setExcelFile(this.reportPath, "Report");
    }

    @BeforeMethod
    public void setUp() {
        this.driver = QlWebdriver.getDriver();
        this.driver.get("https://sachtaodan.vn/account/login");
    }

    @AfterMethod
    public void tearDown() {
        QlWebdriver.closeDriver();
    }

    @Test(
            dataProvider = "loginData"
    )
    public void testLogin(String email, String password, String expectedError, String rowIndexStr) throws Exception {
        int rowIndex = Integer.parseInt(rowIndexStr);
        LoginPage loginPage = new LoginPage(this.driver);
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        Thread.sleep(1000L);
        loginPage.clickLogin();
        Thread.sleep(3000L);
        String actualResult;
        if (this.driver.getCurrentUrl().contains("/account") && !this.driver.getCurrentUrl().contains("/account/login")) {
            if (rowIndex == 1) {
                //  Lấy nội dung block-title trên trang tài khoản
                actualResult = loginPage.getBlockTitleMessage();
            } else {
                actualResult = "Đăng nhập thành công";
            }
        }else {
            actualResult = loginPage.getErrorMessage();
        }

        String status = actualResult.equals(expectedError) ? "Pass" : "Fail";
        System.out.println("TC" + rowIndex + ": Expected: " + expectedError + " | Actual: " + actualResult + " | " + status);
        this.excelHelpers.setCellData(String.valueOf(rowIndex), rowIndex, 0);
        this.excelHelpers.setCellData(email, rowIndex, 1);
        this.excelHelpers.setCellData(password, rowIndex, 2);
        this.excelHelpers.setCellData(expectedError, rowIndex, 3);
        this.excelHelpers.setCellData(actualResult, rowIndex, 4);
        this.excelHelpers.setCellData(status, rowIndex, 5);
    }

    @DataProvider(
            name = "loginData"
    )
    public Object[][] loginDataProvider() {
        Object[][] data = new Object[this.testData.size()][4];

        for(int i = 0; i < this.testData.size(); ++i) {
            data[i] = this.testData.get(i);
        }

        return data;
    }
}