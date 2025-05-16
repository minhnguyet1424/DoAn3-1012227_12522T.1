package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class QlWebdriver {

        private static WebDriver driver;

        public static WebDriver getDriver() {
            if (driver == null) {
                System.setProperty("webdriver.chrome.driver", "C:\\\\Users\\\\Admin\\\\Downloads\\\\chromedriver-win64\\\\chromedriver.exe");
                driver = new ChromeDriver();
                driver.manage().window().maximize();
            }
            return driver;
        }

        public static void closeDriver() {
            if (driver != null) {
                driver.quit();
                driver = null; // Reset lại để lần sau tạo mới
            }
        }
    }

