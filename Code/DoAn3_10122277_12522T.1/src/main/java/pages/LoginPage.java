package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class LoginPage {
    WebDriver driver;
    By emailInput = By.id("email");
    By passwordInput = By.id("pass");
    By loginButton = By.name("send");
    By messageLabel = By.xpath("//li[contains(@style, 'color:red') and contains(@style, 'margin-bottom:10px')]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterEmail(String email) {
        this.driver.findElement(this.emailInput).clear();
        this.driver.findElement(this.emailInput).sendKeys(email);
    }

    public void enterPassword(String password) {
        this.driver.findElement(this.passwordInput).clear();
        this.driver.findElement(this.passwordInput).sendKeys(password);
    }

    public void clickLogin() {
        this.driver.findElement(this.loginButton).click();
    }


    public String getErrorMessage() {
        try {
            return this.driver.findElement(this.messageLabel).getText();
        } catch (Exception e) {
            return "";
        }
    }

}