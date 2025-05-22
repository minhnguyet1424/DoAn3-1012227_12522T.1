 package base;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BaseSetup
{
    public WebDriver setupDriver() {
        return QlWebdriver.getDriver();
    }

    public void closeDriver() {
        QlWebdriver.closeDriver();
    }}