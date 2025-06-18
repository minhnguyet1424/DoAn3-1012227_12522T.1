package pages;

import org.openqa.selenium.*;

public class AddressPage {
    private WebDriver driver;

    public AddressPage(WebDriver driver) {
        this.driver = driver;
    }

    public void fillAddressForm(String firstName, String lastName, String company,
                                String address1, String address2, String city,
                                String country, String zip, String phone) {
        driver.findElement(By.name("first_name")).sendKeys(firstName);
        driver.findElement(By.name("last_name")).sendKeys(lastName);
        driver.findElement(By.name("company")).sendKeys(company);
        driver.findElement(By.name("address1")).sendKeys(address1);
        driver.findElement(By.name("address2")).sendKeys(address2);
        driver.findElement(By.name("city")).sendKeys(city);
        driver.findElement(By.name("country")).sendKeys(country); // hoặc chọn bằng Select nếu là dropdown
        driver.findElement(By.name("zip")).sendKeys(zip);
        driver.findElement(By.name("phone")).sendKeys(phone);
    }

    public void submitAddress() {
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    public String getActualResult() {
        try {
            return driver.findElement(By.className("success-message")).getText(); // tùy trang web
        } catch (Exception e) {
            return "Không thành công";
        }
    }
}
