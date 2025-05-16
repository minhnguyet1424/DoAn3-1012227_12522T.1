package base;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Qlwebdriver {
    private WebDriver driver;
    private WebDriverWait wait;
    JavascriptExecutor js;

    public Qlwebdriver(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }
    public void setText(By element, String value)
    {
        //sendkey 1 giá trị truyền vào
        wait.until(ExpectedConditions.elementToBeClickable(element));
        driver.findElement(element).clear();
        driver.findElement(element).sendKeys(value);
    }
    public void clickElement(By element)
    {
        //click vào 1 ptu elemnet dc truyền
        wait.until(ExpectedConditions.elementToBeClickable(element));
        driver.findElement(element).click();
        //click cuả js
      //  js.executeScript("arguments[0].click();",driver.findElement(element));


    }


}
