package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SearchPage {
    private WebDriver driver;

    public SearchPage(WebDriver driver) {
        this.driver = driver;
    }

    private By searchInput = By.cssSelector("input[name='query']");
    private By searchButton = By.cssSelector("button[type='submit']");

    public void enterKeyword(String keyword) {
        driver.findElement(searchInput).clear();
        driver.findElement(searchInput).sendKeys(keyword);
    }

    public void clickSearch() {
        driver.findElement(searchButton).click();
    }

    public void search(String keyword) {
        enterKeyword(keyword);
        clickSearch();
    }
}
