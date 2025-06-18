package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SignupPage {
    WebDriver driver;

    By firstNameInput = By.id("first_name");
    By lastNameInput = By.id("last_name");
    By emailInput = By.id("email");
    By passwordInput = By.id("creat_password");
    By registerButton = By.cssSelector("input[type='submit'][value='Đăng ký']");
    // Thêm locator lấy các lỗi từ thẻ <ul class="disc">
    By errorMessages = By.cssSelector("ul.disc > li");

    public SignupPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterFirstName(String firstName) {
        driver.findElement(firstNameInput).clear();
        driver.findElement(firstNameInput).sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        driver.findElement(lastNameInput).clear();
        driver.findElement(lastNameInput).sendKeys(lastName);
    }

    public void enterEmail(String email) {
        driver.findElement(emailInput).clear();
        driver.findElement(emailInput).sendKeys(email);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordInput).clear();
        driver.findElement(passwordInput).sendKeys(password);
    }

    public void clickRegister() {
        driver.findElement(registerButton).click();
    }

    public void register(String firstName, String lastName, String email, String password) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        enterPassword(password);
        clickRegister();
    }

    public String getMessage() {
        try {
            return driver.findElement(errorMessages ).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }
}
