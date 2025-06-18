package test;

import base.BaseSetup;
import config.AppURL;
import org.testng.annotations.*;
import pages.OrderPage;
import pages.HomePage;
import utils.ExcelReader;
import utils.ExcelReport;

import java.util.List;

public class OrderTest extends BaseSetup {

List<String[]> testData;

    private HomePage homePage;
    private OrderPage checkoutPage;

    @BeforeClass
    public void setup() throws Exception {
        initializeDriver();
        driver.get(AppURL.TRANG_CHU);
        homePage = new HomePage(driver);
        checkoutPage = new OrderPage(driver);

        testData = ExcelReader.readOrderData("src/test/resources/InputData.xlsx");
        ExcelReport.startNewOrderTest("Order");
    }

    @DataProvider(name = "orderData")
    public Object[][] getOrderData() {
        Object[][] data = new Object[testData.size()][testData.get(0).length];
        for (int i = 0; i < testData.size(); i++) {
            data[i] = testData.get(i);
        }
        return data;
    }

    @Test(dataProvider = "orderData")
    public void testOrder(String bookTitle, String quantity, String email, String fullName, String phone, String address,
                          String province, String district, String ward, String note,
                          String coupon, String paymentMethod, String expectedResult) {
        String actualResult = "", status = "FAIL";

        try {
            driver.get(AppURL.TRANG_CHU);
            homePage.searchBook(bookTitle);
            homePage.selectBookByTitle(bookTitle);

            int quantityInt = (int) Double.parseDouble(quantity); // ✅ số lượng từ Excel
            homePage.setQuantity(quantityInt);
            homePage.addToCart();

            if (!homePage.isPopupDisplayed()) throw new Exception("Không thấy popup xác nhận giỏ hàng");

            checkoutPage = homePage.proceedToCheckoutFromPopup();
            checkoutPage.fillCheckoutForm(email, fullName, phone, address, province, district, ward, note, coupon, paymentMethod);
            checkoutPage.placeOrder();

            boolean success = checkoutPage.isOrderSuccess();
            actualResult = success ? "Đặt hàng thành công" : "Đặt hàng thất bại";
            if (actualResult.equalsIgnoreCase(expectedResult)) status = "PASS";

        } catch (Exception e) {
            actualResult = "Lỗi: " + e.getMessage();
        } finally {
            ExcelReport.writeOrderReport(
                    bookTitle, email, fullName, phone, address, province, district, ward,
                    note, coupon, paymentMethod, expectedResult, actualResult, status
            );

        }
    }

    @AfterClass
    public void tearDown() {
        tearDownAfterTest();
    }
}
