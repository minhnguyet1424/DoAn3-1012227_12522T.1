package test;

import base.QlWebdriver;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import pages.SignupPage;
import utils.ExcelReader;
import utils.ExcelLogger;
import utils.ExcelReport;
import config.AppURL;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SignupTest {
    WebDriver driver;
    String dataPath = "src/test/resources/DataSignup.xlsx";
    List<String[]> testData;
    ExcelLogger logger;

    @BeforeClass
    public void setup() throws Exception {
        this.testData = ExcelReader.readSignupData(dataPath);
        this.driver = QlWebdriver.getDriver();
        ExcelReport.startNewSignupTest("Signup");

        // Khởi tạo logger
        this.logger = new ExcelLogger("test-output/report.xlsx");
    }



    @AfterClass
    public void tearDown() {
        ExcelReport.saveReport();
        QlWebdriver.closeDriver();
    }

    @Test(dataProvider = "signupData")
    public void testSignup(String firstName, String lastName, String email, String password, String expected) throws Exception {
        driver.get(AppURL.SIGNUP);
        SignupPage signup = new SignupPage(driver);
        signup.register(firstName, lastName, email, password);
        Thread.sleep(3000);

        String actual = signup.getMessage();
        String status = actual.equals(expected) ? "Pass" : "Fail";

        // Ghi vào báo cáo tổng
        ExcelReport.writeSignupReport(lastName, firstName, email, password, expected, actual, status);

        // Ghi log chi tiết
        String[] headers = {
                "STT", "Thời gian", "Họ", "Tên", "Email", "Mật khẩu", "Kết quả mong đợi", "Kết quả thực tế", "Trạng thái"
        };
        String[] data = {
                "", // STT để trống, logger sẽ xử lý hoặc để người dùng tự kiểm tra
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()),
                firstName,
                lastName,
                email,
                password,
                expected,
                actual,
                status
        };

        logger.writeRow("Signup", headers, data);
    }

    @DataProvider(name = "signupData")
    public Object[][] signupData() {
        Object[][] data = new Object[testData.size()][5];
        for (int i = 0; i < testData.size(); i++) {
            data[i] = testData.get(i);
        }
        return data;
    }
}
