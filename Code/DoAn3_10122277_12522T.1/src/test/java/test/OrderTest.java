package test;

import base.BaseSetup;
import config.AppURL;
import org.testng.annotations.*;
import pages.CheckoutPage;
import pages.HomePage;
import utils.ExcelReader;
import utils.ExcelReport;

import java.util.List;

public class OrderTest extends BaseSetup {
    private HomePage homePage;
    private CheckoutPage checkoutPage;
    private List<String[]> orderData;

    @BeforeClass
    public void setUp() throws Exception {
        initializeDriver();
        driver.get(AppURL.TRANG_CHU);
        homePage = new HomePage(driver);
        ExcelReport.startNewOrderTest("Order");
        orderData = ExcelReader.readOrderData("src/test/resources/DataOrder.xlsx");
    }

    @DataProvider(name = "orderData")
    public Object[][] getOrderData() {
        Object[][] data = new Object[orderData.size()][orderData.get(0).length];
        for (int i = 0; i < orderData.size(); i++) {
            data[i] = orderData.get(i);
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

            int quantityInt = (int) Double.parseDouble(quantity);// ✅ Lấy số lượng từ file Excel
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
            ExcelReport.writeOrderReport(bookTitle, fullName, phone, address, province, district, ward, note, coupon, paymentMethod, expectedResult, actualResult, status);
        }
    }


    @AfterClass
    public void tearDown() {
        ExcelReport.saveReport();
        closeDriver();
    }
}
