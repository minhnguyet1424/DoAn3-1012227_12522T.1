 package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SearchPage {
    WebDriver driver;

    By searchInput = By.cssSelector("input[placeholder='Tìm kiếm...']");
    By searchButton = By.cssSelector("button[type='submit']");
    By resultText = By.xpath("//p[@class='result-count']");

    public SearchPage(WebDriver driver) {
        this.driver = driver;
    }

    public void searchKeyword(String keyword) {
        driver.findElement(searchInput).clear();
        driver.findElement(searchInput).sendKeys(keyword);
        driver.findElement(searchButton).click();
    }

    public String getSearchResultText() {
        try {
            return driver.findElement(resultText).getText();
        } catch (Exception e) {
            return "";
        }
    }
}