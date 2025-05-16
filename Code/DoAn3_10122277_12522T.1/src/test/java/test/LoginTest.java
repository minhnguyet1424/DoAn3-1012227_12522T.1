package test;

import base.QlWebdriver;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import pages.LoginPage;
import utils.ExcelReader;
import utils.ExcelHelpers;

import java.util.ArrayList;
import java.util.List;

public class LoginTest {

    WebDriver driver;
    String excelPath = "src/test/resources/Book1.xlsx";  // dữ liệu gốc
    String reportPath = "test-output/LoginTestReport.xlsx"; // file báo cáo sẵn có

    ExcelHelpers excelHelpers = new ExcelHelpers();

    List<String[]> testData;

    @BeforeClass
    public void setup() throws Exception {
        // Đọc dữ liệu test
        testData = ExcelReader.readData(excelPath);

        // Mở file báo cáo đã có sẵn (file này bạn tạo trước, có header rồi)
        excelHelpers.setExcelFile(reportPath, "Report");
    }

    @BeforeMethod
    public void setUp() {
        driver = QlWebdriver.getDriver();
        driver.get("https://sachtaodan.vn/account/login");
    }

    @AfterMethod
    public void tearDown() {
        QlWebdriver.closeDriver();
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String email, String password, String expectedError, String rowIndexStr) throws Exception {
        int rowIndex = Integer.parseInt(rowIndexStr);

        LoginPage loginPage = new LoginPage(driver);

        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        Thread.sleep(1000);

        loginPage.clickLogin();
        Thread.sleep(3000);

        String actualResult;
        if (driver.getCurrentUrl().contains("/account") && !driver.getCurrentUrl().contains("/account/login")) {
            actualResult = "Đăng nhập thành công";
        } else {
            actualResult = loginPage.getErrorMessage();
        }

        String status = actualResult.equals(expectedError) ? "Pass" : "Fail";

        System.out.println("TC" + rowIndex + ": Expected: " + expectedError + " | Actual: " + actualResult + " | " + status);

        // Ghi dữ liệu kết quả test vào file báo cáo theo dòng rowIndex
        // Ví dụ cột 0: STT, 1: Email, 2: Password, 3: Expected, 4: Actual, 5: Status
        excelHelpers.setCellData(String.valueOf(rowIndex), rowIndex, 0);
        excelHelpers.setCellData(email, rowIndex, 1);
        excelHelpers.setCellData(password, rowIndex, 2);
        excelHelpers.setCellData(expectedError, rowIndex, 3);
        excelHelpers.setCellData(actualResult, rowIndex, 4);
        excelHelpers.setCellData(status, rowIndex, 5);
    }

    @DataProvider(name = "loginData")
    public Object[][] loginDataProvider() {
        Object[][] data = new Object[testData.size()][4];
        for (int i = 0; i < testData.size(); i++) {
            data[i] = testData.get(i);
        }
        return data;
    }
}
