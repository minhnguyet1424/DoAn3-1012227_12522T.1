//package test;
//
//import base.BaseSetup;
//import org.testng.annotations.*;
//import pages.AddressPage;
//import utils.ExcelReader;
//import utils.ExcelReport;
//
//import java.util.List;
//
//public class AddressTest extends BaseSetup {
//
//    @Test
//    public void testAddAddress() {
//        String excelPath = "src/test/resources/DataAddress.xlsx";
//        List<String[]> testData = ExcelReader.readAddAddressData(excelPath);
//
//        driver.get("https://sachtaodan.vn/account/addresses"); // đường dẫn tới trang sổ địa chỉ
//
//        ExcelReport.startNewAddAddressTest("AddAddress");
//
//        for (String[] row : testData) {
//            String firstName = row[0];
//            String lastName = row[1];
//            String company = row[2];
//            String address1 = row[3];
//            String address2 = row[4];
//            String city = row[5];
//            String country = row[6];
//            String zip = row[7];
//            String phone = row[8];
//            String expected = row[9];
//
//            AddressPage page = new AddressPage(driver);
//            page.fillAddressForm(firstName, lastName, company, address1, address2, city, country, zip, phone);
//            page.submitAddress();
//
//            String actual = page.getActualResult();
//            String status = actual.contains(expected) ? "Pass" : "Fail";
//
//            ExcelReport.writeAddAddressReport(firstName, lastName, company, address1, address2, city, country, zip, phone, expected, actual, status);
//        }
//
//        ExcelReport.saveReport();
//    }
//}
