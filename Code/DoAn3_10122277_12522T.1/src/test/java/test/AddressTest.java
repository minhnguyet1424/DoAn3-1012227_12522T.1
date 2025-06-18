package test;

import base.BaseSetup;
import config.AppURL;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import pages.AddAddressPage;
import pages.LoginPage;
import utils.ExcelReader;
import utils.ExcelReport;

import java.util.List;

public class AddAddressTest extends BaseSetup {
    List<String[]> testData;

    @BeforeClass
    public void setup() throws Exception {
        initializeDriver();
        // Đọc dữ liệu từ file dataaddress.xlsx
        testData = ExcelReader.readData("src/test/resources/DataAddress.xlsx");
        ExcelReport.startNewAddAddressTest("AddAddress"); // Sheet báo cáo tên: AddAddress
    }


    @AfterClass
    public void tearDown() {
        ExcelReport.saveReport(); // Lưu file báo cáo sau khi test xong
        closeDriver();
    }

    @Test(dataProvider = "addressData")
    public void testAddAddress(String firstName, String lastName, String company, String address1, String address2,
                               String city, String country, String zip, String phone, String expected) throws Exception {

        // === Đăng nhập trước ===
        driver.get(AppURL.LOGIN);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterEmail("your_email_here@gmail.com"); // ← Thay bằng email thật
        loginPage.enterPassword("your_password_here");     // ← Thay bằng mật khẩu thật
        loginPage.clickLogin();
        Thread.sleep(3000);

        // === Truy cập Sổ địa chỉ ===
        driver.get("https://sachtaodan.vn");
        AddAddressPage addressPage = new AddAddressPage(driver);

        // === Thêm địa chỉ ===
        addressPage.openAddAddressForm();
        addressPage.fillAddressForm(firstName, lastName, company, address1, address2, city, country, zip, phone);
        addressPage.clickAddAddressButton();
        Thread.sleep(3000);

        // === Kiểm tra kết quả và ghi báo cáo ===
        String actual = addressPage.getConfirmationMessage();
        String status = actual.contains(expected) ? "Pass" : "Fail";

        ExcelReport.writeAddAddressReport(firstName, lastName, company, address1, address2,
                city, country, zip, phone, expected, actual, status);
    }

    @DataProvider(name = "addressData")
    public Object[][] addressDataProvider() {
        Object[][] data = new Object[testData.size()][10]; // 10 cột đúng theo Excel
        for (int i = 0; i < testData.size(); i++) {
            data[i] = testData.get(i);
        }
        return data;
    }
}
