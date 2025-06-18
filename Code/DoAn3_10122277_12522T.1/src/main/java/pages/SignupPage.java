package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class SignupPage {
    WebDriver driver;

    // Locator
    By firstNameInput = By.id("first_name");
    By lastNameInput = By.id("last_name");
    By emailInput = By.id("email");
    By passwordInput = By.id("creat_password");
    By registerButton = By.cssSelector("input[type='submit'][value='Đăng ký']");
    By errorMessages = By.cssSelector("ul.disc > li");

    public SignupPage(WebDriver driver) {
        this.driver = driver;
    }

    // === Hành động nhập dữ liệu ===
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

    // === Đăng ký tài khoản ===
    public void register(String firstName, String lastName, String email, String password) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        enterPassword(password);
        clickRegister();
    }

    // === Lấy tất cả thông báo lỗi/thành công sau khi đăng ký ===
    public String getAllMessages() {
        try {
            List<WebElement> messages = driver.findElements(errorMessages);
            return messages.stream()
                    .map(WebElement::getText)
                    .filter(msg -> !msg.trim().isEmpty())
                    .collect(Collectors.joining(" | "));
        } catch (Exception e) {
            return "";
        }
    }

    // === (Tuỳ chọn) Lấy dòng lỗi đầu tiên nếu chỉ cần một dòng ===
    public String getMessage() {
        try {
            return driver.findElement(errorMessages).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }
}
