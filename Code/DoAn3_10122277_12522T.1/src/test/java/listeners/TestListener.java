package listeners;

import base.QlWebdriver;
import org.openqa.selenium.WebDriver; // ✅ Bắt buộc thêm dòng này
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ScreenshotUtil;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Test FAILED: " + result.getName());
        WebDriver driver = QlWebdriver.getDriver(); //
        ScreenshotUtil.takeScreenshot(driver, result.getName()); //
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println(" Test PASSED: " + result.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println(" Test STARTED: " + result.getName());
    }

    @Override public void onTestSkipped(ITestResult result) {}
    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
    @Override public void onStart(ITestContext context) {}
    @Override public void onFinish(ITestContext context) {}
}
