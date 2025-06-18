package base;

import org.openqa.selenium.WebDriver;
import utils.ExcelReport;

public class BaseSetup {
    protected WebDriver driver;

    public void initializeDriver() {
        driver = QlWebdriver.getDriver(); // Lấy driver đã quản lý từ QlWebdriver
    }

    public void closeDriver() {
        QlWebdriver.closeDriver(); // Đóng driver an toàn
    }
    public void tearDownAfterTest() {
        ExcelReport.saveReport();
        closeDriver();
    }
}
