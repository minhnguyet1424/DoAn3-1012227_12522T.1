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

    // Điền form thanh toán
    public void fillCheckoutForm(String email, String fullName, String phone, String address,
                                 String province, String district, String ward,
                                 String note, String coupon, String paymentMethod) {

        // Nhập các thông tin cơ bản
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys(email);

        driver.findElement(By.name("billingName")).clear();
        driver.findElement(By.name("billingName")).sendKeys(fullName);

        driver.findElement(By.name("billingPhone")).clear();
        driver.findElement(By.name("billingPhone")).sendKeys(phone);

        driver.findElement(By.name("billingAddress")).clear();
        driver.findElement(By.name("billingAddress")).sendKeys(address);

        // Dropdown Select2: Tỉnh → Quận → Phường (theo đúng ID container)
        selectDropdownBySelect2Id("select2-billingProvince-container", province); // Tỉnh
        sleep(1000);
        selectDropdownBySelect2Id("select2-billingDistrict-container", district);  // Quận
        sleep(1000);
        selectDropdownBySelect2Id("select2-billingWard-container", ward);          // Phường

        // Ghi chú nếu có
        if (note != null && !note.trim().isEmpty()) {
            try {
                WebElement noteField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("note")));
                noteField.clear();
                noteField.sendKeys(note);
            } catch (Exception e) {
                System.out.println(" Không tìm thấy trường ghi chú: " + e.getMessage());
            }
        }

        // Mã giảm giá nếu có
        if (coupon != null && !coupon.trim().isEmpty()) {
            try {
                WebElement couponInput = driver.findElement(By.name("discount_code"));
                couponInput.clear();
                couponInput.sendKeys(coupon);
                driver.findElement(By.xpath("//button[contains(text(),'Áp dụng')]")).click();
            } catch (Exception e) {
                System.out.println(" Không thể áp dụng mã giảm giá: " + e.getMessage());
            }
        }

        //  Chọn phương thức thanh toán (sửa lại đoạn này cho chính xác)
        try {
            if (paymentMethod.toLowerCase().contains("cod") || paymentMethod.toLowerCase().contains("khi nhận hàng")) {
                WebElement codRadio = wait.until(ExpectedConditions.elementToBeClickable(By.id("paymentMethod-456398")));
                if (!codRadio.isSelected()) {
                    codRadio.click();
                }
            } else {
                WebElement bankRadio = wait.until(ExpectedConditions.elementToBeClickable(By.id("paymentMethod-306329")));
                if (!bankRadio.isSelected()) {
                    bankRadio.click();
                }
            }
        } catch (Exception e) {
            System.out.println(" Không thể chọn phương thức thanh toán: " + e.getMessage());
        }
    }

    //  Hàm xử lý dropdown Select2 theo ID chính xác
    private void selectDropdownBySelect2Id(String select2ContainerId, String valueToSelect) {
        try {
            // Mở dropdown
            String containerXpath = "//span[@id='" + select2ContainerId + "']";
            WebElement container = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(containerXpath)));
            container.click();

            // Gõ để tìm
            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input.select2-search__field")));
            input.sendKeys(valueToSelect);
            Thread.sleep(500); // chờ hiển thị gợi ý

            // Chọn đúng mục
            String resultXpath = "//li[contains(@class,'select2-results__option') and normalize-space()=\"" + valueToSelect + "\"]";
            WebElement result = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(resultXpath)));
            result.click();

        } catch (Exception e) {
            throw new RuntimeException(" Không thể chọn giá trị '" + valueToSelect + "' cho dropdown ID: " + select2ContainerId, e);
        }
    }

    public void placeOrder() {
        try {
            // Xác định đúng nút ĐẶT HÀNG
            By buttonLocator = By.xpath("//button[contains(@class, 'btn-checkout') and .//span[text()='ĐẶT HÀNG']]");

            // Đợi phần tử hiển thị trên DOM
            WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(buttonLocator));

            // Chờ tới khi class không chứa 'spinner--active' nữa (tức là không đang submit)
            wait.until(driver -> {
                String classAttr = button.getAttribute("class");
                return classAttr != null && !classAttr.contains("spinner--active");
            });

            // Cuộn đến nút để tránh bị che khuất
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", button);
            Thread.sleep(500);

            // Dùng JavaScript để click
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);

        } catch (Exception e) {
            throw new RuntimeException(" Không thể click nút 'ĐẶT HÀNG': " + e.getMessage(), e);
        }
    }


    public boolean isOrderSuccess() {
        try {
            // Chờ phần tử <h2> chứa nội dung xác nhận thành công
            WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h2[contains(text(),'Cảm ơn bạn đã đặt hàng')]")
            ));

            // Trả lại true nếu nội dung khớp
            return successMsg != null && successMsg.isDisplayed();
        } catch (TimeoutException e) {
            System.out.println(" Không tìm thấy thông báo thành công: " + e.getMessage());
            return false;
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}
