package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class OrderPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // === Nhập thông tin giao hàng ===
    public void fillShippingInfo(String fullName, String phone, String address,
                                 String province, String district, String ward,
                                 String note, String coupon, String paymentMethod) throws InterruptedException {

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("billingName"))).sendKeys(fullName);
        driver.findElement(By.id("billingPhone")).sendKeys(phone);
        driver.findElement(By.id("billingAddress")).sendKeys(address);

        // Chọn tỉnh/thành phố
        selectDropdown("(//span[@role='combobox'])[1]", province);
        Thread.sleep(1000); // chờ load quận

        // Chọn quận/huyện
        selectDropdown("(//span[@role='combobox'])[2]", district);
        Thread.sleep(1000); // chờ load xã

        // Chọn xã/phường
        selectDropdown("(//span[@role='combobox'])[3]", ward);

        // Ghi chú
        if (note != null && !note.trim().isEmpty()) {
            WebElement noteField = driver.findElement(By.id("note"));
            noteField.clear();
            noteField.sendKeys(note);
        }

        // Mã giảm giá (nếu có)
        if (coupon != null && !coupon.trim().isEmpty()) {
            WebElement couponField = driver.findElement(By.name("discount"));
            couponField.sendKeys(coupon);
            driver.findElement(By.xpath("//button[contains(text(),'Áp dụng')]")).click();
            Thread.sleep(1000); // chờ áp dụng mã
        }

        // Phương thức thanh toán
        selectPaymentMethod(paymentMethod);
    }

    // === Chọn phương thức thanh toán ===
    public void selectPaymentMethod(String method) {
        if (method == null || method.trim().isEmpty()) return;

        if (method.toLowerCase().contains("chuyển khoản")) {
            clickIfVisible(By.xpath("//div[contains(text(),'Chuyển khoản')]"));
        } else if (method.toLowerCase().contains("thanh toán khi nhận hàng") || method.toLowerCase().contains("cod")) {
            clickIfVisible(By.xpath("//div[contains(text(),'Thanh toán khi nhận hàng')]"));
        }
    }

    // === Nhấn nút Đặt hàng ===
    public void placeOrder() {
        WebElement orderBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit']//span[contains(text(),'Đặt hàng')]")));
        orderBtn.click();
    }

    // === Hàm phụ trợ: chọn item trong dropdown Select2 ===
    private void selectDropdown(String comboXpath, String textToSelect) {
        WebElement combo = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(comboXpath)));
        combo.click();

        WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//li[contains(text(),'" + textToSelect + "')]")));
        option.click();
    }

    // === Hàm phụ trợ: click nếu phần tử hiển thị ===
    private void clickIfVisible(By locator) {
        try {
            WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
            el.click();
        } catch (TimeoutException ignored) {
        }
    }
}
