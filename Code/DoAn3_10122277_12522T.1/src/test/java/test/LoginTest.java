package test;

import base.BaseSetup;
import config.AppURL;
import org.testng.annotations.*;
import pages.LoginPage;
import utils.ExcelReader;
import utils.ExcelReport;

import java.util.List;

public class LoginTest extends BaseSetup {
    List<String[]> testData;

    @BeforeClass
    public void setup() throws Exception {
        initializeDriver();
        testData = ExcelReader.readLoginData("src/test/resources/InputData.xlsx");
        ExcelReport.startNewLoginTest("Login");
    }
    @AfterClass
    public void tearDown() {
        tearDownAfterTest();
    }
    @Test(dataProvider = "loginData")
    public void testLogin(String email, String password, String expectedError, String rowIndexStr) throws Exception {
        int rowIndex = Integer.parseInt(rowIndexStr);
        // Luôn trở về trang login trước mỗi test
        if (!driver.getCurrentUrl().contains("/account/login")) {
            driver.get("https://sachtaodan.vn/account/logout");
            Thread.sleep(2000);
        }
        driver.get(AppURL.LOGIN);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLogin();
        Thread.sleep(3000);  // đợi xử lý login

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
