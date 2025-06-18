package test;

import base.BaseSetup;
import config.AppURL;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import pages.LoginPage;
import utils.ExcelReader;
import utils.ExcelReport;

import java.util.List;

public class LoginTest extends BaseSetup {  // ✅ Kế thừa BaseSetup
    List<String[]> testData;

    @BeforeClass
    public void setup() throws Exception {
        initializeDriver(); // ✅ Sử dụng phương thức từ BaseSetup
        testData = ExcelReader.readData("src/test/resources/Book1.xlsx");
        ExcelReport.startNewLoginTest("Login");
    }

    @AfterClass
    public void tearDown() {
        ExcelReport.saveReport();
        closeDriver(); // ✅ Sử dụng phương thức từ BaseSetup
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String email, String password, String expectedError, String rowIndexStr) throws Exception {
        int rowIndex = Integer.parseInt(rowIndexStr);

        // Đảm bảo luôn về trang login
        if (!driver.getCurrentUrl().contains("/account/login")) {
            driver.get("https://sachtaodan.vn/account/logout");
            Thread.sleep(2000);
        }
        driver.get(AppURL.LOGIN);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLogin();
        Thread.sleep(3000);

        String actualResult;
        if (driver.getCurrentUrl().contains("/account") && !driver.getCurrentUrl().contains("/account/login")) {
            actualResult = "Đăng nhập thành công";
        } else {
            actualResult = loginPage.getErrorMessage();
        }

        String status = actualResult.equals(expectedError) ? "Pass" : "Fail";
        ExcelReport.writeLoginReport(email, password, expectedError, actualResult, status);
    }

    @DataProvider(name = "loginData")
    public Object[][] loginDataProvider() {
        Object[][] data = new Object[testData.size()][4];
        for (int i = 0; i < testData.size(); ++i) {
            data[i] = testData.get(i);
        }
        return data;
    }
}
