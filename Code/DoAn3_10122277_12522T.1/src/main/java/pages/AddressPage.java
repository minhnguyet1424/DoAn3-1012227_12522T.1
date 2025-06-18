package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class AddAddressPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public AddAddressPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Mở form thêm địa chỉ
    public void openAddAddressForm() {
        WebElement btnShowForm = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@onclick='Bizweb.CustomerAddress.toggleNewForm(); return false;'][contains(text(),'Thêm địa chỉ')]")));
        btnShowForm.click();
    }

    // Nhập thông tin địa chỉ mới
    public void fillAddressForm(String firstName, String lastName, String company, String address1,
                                String address2, String city, String country, String zip, String phone) {

        WebElement container = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add_address")));

        container.findElement(By.xpath(".//input[@placeholder='Tên']")).sendKeys(firstName);
        container.findElement(By.xpath(".//input[@placeholder='Họ']")).sendKeys(lastName);
        container.findElement(By.xpath(".//input[@placeholder='Công ty']")).sendKeys(company);
        container.findElement(By.xpath(".//input[@placeholder='Địa chỉ 1']")).sendKeys(address1);
        container.findElement(By.xpath(".//input[@placeholder='Địa chỉ 2']")).sendKeys(address2);
        container.findElement(By.xpath(".//input[@placeholder='Thành phố']")).sendKeys(city);
        container.findElement(By.xpath(".//input[@placeholder='Zip']")).sendKeys(zip);
        container.findElement(By.xpath(".//input[@placeholder='Số điện thoại']")).sendKeys(phone);

        Select countryDropdown = new Select(container.findElement(By.id("address_country_")));
        countryDropdown.selectByVisibleText(country);
    }

    // Click nút “Thêm địa chỉ”
    public void clickAddAddressButton() {
        WebElement btnAdd = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='add_address']//input[@value='Thêm địa chỉ']")));
        btnAdd.click();
    }

    // Lấy thông báo sau khi thêm địa chỉ (nếu có)
    public String getConfirmationMessage() {
        try {
            WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.className("alert-success")));
            return alert.getText().trim();
        } catch (TimeoutException e) {
            return "Không thấy thông báo";
        }
    }
}
