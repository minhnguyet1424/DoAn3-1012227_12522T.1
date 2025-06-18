package test;

import base.BaseSetup;
import base.QlWebdriver;
import config.AppURL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;
import pages.SignupPage;
import utils.ExcelReader;
import utils.ExcelReport;

import java.time.Duration;
import java.util.List;

public class SignupTest extends BaseSetup {
    List<String[]> testData;
    @BeforeClass
    public void setUp() throws Exception {
        initializeDriver();
        driver.get(AppURL.TRANG_CHU);
        ExcelReport.startNewSignupTest("Signup");
        testData = ExcelReader.readSignupData("src/test/resources/InputData.xlsx");
    }
    @AfterClass
    public void tearDown() {
        tearDownAfterTest();
    }
    @Test(dataProvider = "signupData")
    public void testSignup(String firstName, String lastName, String email, String password, String expected) throws Exception {
        driver.get(AppURL.SIGNUP);
        SignupPage signup = new SignupPage(driver);
        signup.register(firstName, lastName, email, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("ul.disc > li")),
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(".note, .error, .success"))
        ));

        String actual = signup.getAllMessages();
        String status = actual.contains(expected) ? "Pass" : "Fail";

        ExcelReport.writeSignupReport(firstName, lastName, email, password, expected, actual, status);
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
