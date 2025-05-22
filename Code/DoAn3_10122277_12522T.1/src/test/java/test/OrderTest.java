package test;

import base.BaseSetup;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import pages.OrderPage;
import utils.ExcelHelpers;
import utils.ExcelLogger;

public class OrderTest extends BaseSetup {
    @Test
    public void testPlaceOrder() throws Exception {
        ExcelHelpers excel = new ExcelHelpers();
        ExcelLogger logger = new ExcelLogger();
        excel.setExcelFile("src/test/resources/orderdata.xlsx", "Sheet1");

        for (int i = 1; i <= excel.getRowCount(); i++) {
            String keyword = excel.getCellData("Keyword", i);
            String email = excel.getCellData("Email", i);
            String name = excel.getCellData("Name", i);
            String phone = excel.getCellData("Phone", i);
            String address = excel.getCellData("Address", i);
            String province = excel.getCellData("Province", i);
            String district = excel.getCellData("District", i);
            String ward = excel.getCellData("Ward", i);
            String expectedResult = excel.getCellData("ExpectedResult", i);

            driver.get("https://sachtaodan.com");
            driver.findElement(By.name("q")).sendKeys(keyword);
            driver.findElement(By.cssSelector("button[type='submit']")).click();
            Thread.sleep(2000);
            driver.findElements(By.cssSelector(".product-item a")).get(0).click();
            driver.findElement(By.xpath("//button[contains(text(),'Thêm vào giỏ hàng')]")).click();
            Thread.sleep(1000);
            driver.get("https://sachtaodan.com/cart");
            driver.findElement(By.xpath("//a[contains(text(),'Tiến hành đặt hàng')]")).click();

            OrderPage order = new OrderPage(driver);
            order.enterEmail(email);
            order.enterName(name);
            order.enterPhone(phone);
            order.enterAddress(address);
            order.selectProvince(province);
            order.selectDistrict(district);
            order.selectWard(ward);
            order.chooseCOD();
            Thread.sleep(1000);
            order.clickOrder();
            Thread.sleep(3000);

            boolean actual = order.isOrderSuccess();
            String result = actual ? "PASS" : "FAIL";
            logger.writeLog("Đặt hàng", result);

            System.out.println("Kết quả test dòng " + i + ": " + result);
        }
    }
}
