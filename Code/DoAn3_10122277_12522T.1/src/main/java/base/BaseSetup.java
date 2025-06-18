package base;

import org.openqa.selenium.WebDriver;

public class BaseSetup {
    protected WebDriver driver;

    public void initializeDriver() {
        driver = QlWebdriver.getDriver();
    }

    public void closeDriver() {
        QlWebdriver.closeDriver();
    }
}
