package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.Duration;
public class SingInTest {
    private WebDriver driver;
    private ValidateHelper  validateHelper;

    private By loginBtn= By.id("btn-login");

    public void singIn() throws InterruptedException{

        driver=new ChromeDriver();
        driver.manage().window().maximize();

        validateHelper = new ValidateHelper(driver);
        driver.get("https://sachtaodan.vn/");

        validateHelper.clickElement(loginBtn);

        Thread.sleep(2000);
        driver.quit();
    }
}
