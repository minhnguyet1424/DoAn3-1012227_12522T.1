package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By searchBox = By.xpath("//input[@placeholder='Tìm kiếm...']");
    private By quantityInput = By.id("qty"); //  đúng theo HTML bạn cung cấp
    private By increaseQtyBtn = By.cssSelector("button.increase.count");
    private By decreaseQtyBtn = By.cssSelector("button.reduced.count");
    private By addToCartBtn = By.xpath("//button[contains(.,'Mua ngay')]");
    private By popupContent = By.xpath("//div[@class='content']");
    private By proceedToCheckoutBtn = By.xpath("//button[contains(text(),'Tiến hành thanh toán')]");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Gõ từ khóa tìm sách vào ô tìm kiếm và nhấn Enter
    public void searchBook(String keyword) {
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
        searchInput.clear();
        searchInput.sendKeys(keyword);
        searchInput.sendKeys(Keys.ENTER);
    }

    // Chọn cuốn sách khớp chính xác tiêu đề từ kết quả tìm kiếm
    public void selectBookByTitle(String bookTitle) {
        String xpath = "//a[@class='text2line' and contains(normalize-space(), \"" + bookTitle + "\")]";
        try {
            WebElement bookLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
            bookLink.click();
        } catch (Exception e) {
            throw new RuntimeException("Không tìm thấy sách: " + bookTitle);
        }
    }

    // Thay đổi số lượng bằng cách dùng nút +/-
    public void setQuantity(int quantity) {
        WebElement qtyInput = wait.until(ExpectedConditions.visibilityOfElementLocated(quantityInput));
        int currentQty = Integer.parseInt(qtyInput.getAttribute("value").trim());

        if (currentQty == quantity) return;

        if (quantity > currentQty) {
            WebElement plusBtn = wait.until(ExpectedConditions.elementToBeClickable(increaseQtyBtn));
            for (int i = currentQty; i < quantity; i++) {
                plusBtn.click();
                try { Thread.sleep(300); } catch (InterruptedException ignored) {}
            }
        } else {
            WebElement minusBtn = wait.until(ExpectedConditions.elementToBeClickable(decreaseQtyBtn));
            for (int i = currentQty; i > quantity; i--) {
                minusBtn.click();
                try { Thread.sleep(300); } catch (InterruptedException ignored) {}
            }
        }

        // Kiểm tra kết quả sau khi điều chỉnh
        String actual = qtyInput.getAttribute("value").trim();
        if (!actual.equals(String.valueOf(quantity))) {
            throw new RuntimeException("Không thể set số lượng: " + quantity + ". Giá trị thực tế: " + actual);
        }
    }

    // Nhấn nút "Mua ngay" (Thêm vào giỏ hàng)
    public void addToCart() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(addToCartBtn));
        button.click();
    }

    // Kiểm tra popup thêm giỏ hàng đã hiển thị hay chưa
    public boolean isPopupDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(popupContent)).isDisplayed();
    }

    // Nhấn nút "Tiến hành thanh toán" từ popup
    public OrderPage proceedToCheckoutFromPopup() {
        wait.until(ExpectedConditions.elementToBeClickable(proceedToCheckoutBtn)).click();
        return new OrderPage(driver);
    }
}
