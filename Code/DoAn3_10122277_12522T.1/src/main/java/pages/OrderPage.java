package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class OrderPage {
    private WebDriver driver;

    public OrderPage(WebDriver driver) {
        this.driver = driver;
    }

    // Selectors
    private By emailField = By.id("checkout_user_email");
    private By nameField = By.id("checkout_user_name");
    private By phoneField = By.id("checkout_user_phone");
    private By addressField = By.id("checkout_user_address");
    private By provinceDropdown = By.id("checkout_user_province_id");
    private By districtDropdown = By.id("checkout_user_district_id");
    private By wardDropdown = By.id("checkout_user_ward_id");
    private By codOption = By.cssSelector("input[value='cod']");
    private By orderButton = By.xpath("//button[contains(text(),'ĐẶT HÀNG')]");
    private By successMessage = By.xpath("//*[contains(text(),'đặt hàng thành công') or contains(text(),'Đơn hàng của bạn đã được ghi nhận')]");

    // Actions
    public void enterEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }

    public void enterName(String name) {
        driver.findElement(nameField).sendKeys(name);
    }

    public void enterPhone(String phone) {
        driver.findElement(phoneField).sendKeys(phone);
    }

    public void enterAddress(String address) {
        driver.findElement(addressField).sendKeys(address);
    }

    public void selectProvince(String provinceName) throws InterruptedException {
        Select select = new Select(driver.findElement(provinceDropdown));
        select.selectByVisibleText(provinceName);
        Thread.sleep(1000); // chờ load huyện
    }

    public void selectDistrict(String districtName) throws InterruptedException {
        Select select = new Select(driver.findElement(districtDropdown));
        select.selectByVisibleText(districtName);
        Thread.sleep(1000); // chờ load xã
    }

    public void selectWard(String wardName) {
        Select select = new Select(driver.findElement(wardDropdown));
        select.selectByVisibleText(wardName);
    }

    public void chooseCOD() {
        driver.findElement(codOption).click();
    }

    public void clickOrder() {
        driver.findElement(orderButton).click();
    }

    public boolean isOrderSuccess() {
        return driver.findElements(successMessage).size() > 0;
    }
}
