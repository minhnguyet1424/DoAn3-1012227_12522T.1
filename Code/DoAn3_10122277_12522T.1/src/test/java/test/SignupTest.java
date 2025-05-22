package test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.SignupPage;
import utils.ExcelReader;
import utils.ExcelReport;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SignupTest {
    WebDriver driver;
    SignupPage signupPage;
    static ExcelReport report;
    static int stt = 1;

    @BeforeClass
    public void initReport() {
        // Tạo file report với header
        List<String> headers = Arrays.asList("STT", "Họ", "Tên", "Email", "Mật khẩu", "Kết quả mong đợi ", "Kết quả thực tế", "Trạng thái");
        report = new ExcelReport("test-output/SignupReport.xlsx", headers);

    }

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://sachtaodan.vn/account/register");
        signupPage = new SignupPage(driver);
    }

    @DataProvider(name = "signupData")
    public Object[][] signupData() {
        List<String[]> dataList = ExcelReader.readSignupData("src/test/resources/DataSignup.xlsx");
        Object[][] dataArray = new Object[dataList.size()][];
        return dataList.toArray(dataArray);
    }

    @Test(dataProvider = "signupData")
    public void testSignup(String lastName, String firstName, String email, String password, String expectedMessage) {
        signupPage.signup(lastName, firstName, email, password);

        String actualMessage = signupPage.getMessage();
        boolean passed = actualMessage.trim().equals(expectedMessage.trim());

        // Ghi vào report
        report.writeSignupTestResult(stt, lastName, firstName, email, password, expectedMessage, actualMessage, passed ? "PASSED" : "FAILED");

        // Kiểm tra kết quả
        Assert.assertEquals(actualMessage.trim(), expectedMessage.trim(), "Sai message tại case: " + email);

        stt++;
    }

    @AfterMethod
    public void teardown() {
        driver.quit();
    }

    @AfterClass
    public void closeReport() throws IOException {
        report.close();
    }
}
