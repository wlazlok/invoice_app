package app.invoice;

import app.invoice.models.*;

import java.util.Arrays;
import java.util.Date;

public class MockModel {

    public Contractor generateContractor() {
        Contractor contractor = new Contractor();

        contractor.setId(1L);
        contractor.setEmail("email@test.com");
        contractor.setNip("123123123");
        contractor.setCompanyName("Company name");
        contractor.setStreet("Street 12");
        contractor.setPostalCode("12-123");
        contractor.setCity("City");

        return contractor;
    }

    public User generateUserFromContext() {
        User user = new User();

        user.setId(100L);
        user.setUsername("user");
        user.setPassword("password");
        user.setEmail("email@test.com");
        user.setNip("123123123");
        user.setStreet("Test Street");
        user.setPostalCode("12-234");
        user.setCity("Test City");
        user.setBankAccountNumber("123123123213");
        user.setConfirmPassword("password");

        return user;
    }

    public Contractor generateContractorWithErrors() {
        Contractor contractor = new Contractor();

        contractor.setId(1L);
        contractor.setEmail("email@test.com");
        contractor.setNip(null);
        contractor.setCompanyName("Company name");
        contractor.setStreet("Street 12");
        contractor.setPostalCode("12-123");
        contractor.setCity("City");

        return contractor;
    }

    public GoodsAndServices generateGoodsAndServices() {
        GoodsAndServices goodsAndServices = new GoodsAndServices();
        goodsAndServices.setUser(generateUserFromContext());
        goodsAndServices.setPrice(12.2);
        goodsAndServices.setName("test");
        goodsAndServices.setUnit("unit");
        goodsAndServices.setId(1L);
        return goodsAndServices;
    }
    public GoodsAndServices generateGoodsAndServicesWithErrors() {
        GoodsAndServices goodsAndServices = new GoodsAndServices();
        goodsAndServices.setUser(generateUserFromContext());
        goodsAndServices.setPrice(null);
        goodsAndServices.setName("test");
        goodsAndServices.setUnit("unit");
        goodsAndServices.setId(1L);
        return goodsAndServices;
    }

    public Invoice generateInvoice() {
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setInvoiceNumber("123-TEST");
        invoice.setDateOfIssue(new Date());
        invoice.setCity("Cracow");
        invoice.setSaleDate(new Date());
        invoice.setPaymentDate(new Date());
        invoice.setPayingMethod(PayingMethods.card);
        invoice.setTotalPrice(123.42);
        invoice.setSeenDate(new Date());
        return invoice;
    }

}
