package app.invoice;

import app.invoice.models.*;
import java.util.*;

public class BuildModels {

    public static ChangeUserPasswordForm changeUserPasswordFormWithAllFieldsNull() {
        ChangeUserPasswordForm changeUserPasswordForm = new ChangeUserPasswordForm();
        changeUserPasswordForm.setNewPassword(null);
        changeUserPasswordForm.setOldPassword(null);
        changeUserPasswordForm.setConfirmNewPassword(null);
        changeUserPasswordForm.setUserName(null);
        return changeUserPasswordForm;
    }

    public static Contractor contractorWithNullAlFields() {
        Contractor contractor = new Contractor();
        contractor.setEmail(null);
        contractor.setNip(null);
        contractor.setCompanyName(null);
        contractor.setStreet(null);
        contractor.setPostalCode(null);
        contractor.setCity(null);

        return contractor;
    }

    public static GoodsAndServices goodsAndServicesWithNullAllFields() {
        GoodsAndServices goodsAndServices = new GoodsAndServices();
        goodsAndServices.setUser(null);
        goodsAndServices.setPrice(null);
        goodsAndServices.setName(null);
        goodsAndServices.setUnit(null);
        goodsAndServices.setId(null);

        return goodsAndServices;
    }

    public static Invoice InvoiceWithNullAllFields() {
        Invoice invoice = new Invoice();

        invoice.setId(null);
        invoice.setInvoiceNumber(null);
        invoice.setDateOfIssue(null);
        invoice.setCity(null);
        invoice.setSaleDate(null);
        invoice.setPaymentDate(null);
        invoice.setPayingMethod(null);
        invoice.setTotalPrice(null);
//        invoice.setDealerId(null);
//        invoice.setBuyerId(null);
        invoice.setSeenDate(null);

        return invoice;
    }

    public static ResetPasswordForm resetPasswordFormWithNullAllFields() {
        ResetPasswordForm resetPasswordForm = new ResetPasswordForm();
        resetPasswordForm.setEmail(null);
        resetPasswordForm.setUserName(null);
        return resetPasswordForm;
    }

    public static User UserWithNullAllFields() {
        User user = new User();
        user.setId(null);
        user.setUsername(null);
        user.setPassword(null);
        user.setEmail(null);
        user.setNip(null);
        user.setCompanyName(null);
        user.setStreet(null);
        user.setPostalCode(null);
        user.setCity(null);
        user.setBankAccountNumber(null);
        user.setConfirmPassword(null);

        return user;
    }

    public static List<User> mockUsers() {
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

        User user1 = new User();
        user1.setId(101L);
        user1.setUsername("user1");
        user1.setPassword("password");
        user1.setEmail("email@test.com");
        user1.setNip("123123123");
        user1.setStreet("Test Street");
        user1.setPostalCode("12-234");
        user1.setCity("Test City");
        user1.setBankAccountNumber("123123123213");
        user1.setConfirmPassword("password");

        User user2 = new User();
        user2.setId(102L);
        user2.setUsername("user2");
        user2.setPassword("password");
        user2.setEmail("email123@test.com");
        user2.setNip("123123123");
        user2.setStreet("Test Street 44");
        user2.setPostalCode("33-555");
        user2.setCity("Test New City");
        user2.setBankAccountNumber("123123123213");
        user2.setConfirmPassword("password");

        return Arrays.asList(user, user1, user2);
    }

    public static Invoice mockInvoice() {
        GoodsAndServices goodsAndServices = new GoodsAndServices();
        goodsAndServices.setId(1L);
        goodsAndServices.setName("Test");
        goodsAndServices.setPrice(12.12);
        goodsAndServices.setUnit("m3");

        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setInvoiceNumber("ABC-122");
        invoice.setDateOfIssue(new Date());
        invoice.setCity("Cracow");
        invoice.setSaleDate(new Date());
        invoice.setPaymentDate(new Date());
        invoice.setPayingMethod(PayingMethods.card);
        invoice.setSeenDate(new Date());
        invoice.setGood(goodsAndServices);

        return invoice;
    }

    public static Contractor mockContractor() {
        Contractor contractor = new Contractor();
        contractor.setId(1L);
        contractor.setEmail("contractor@emial.com");
        contractor.setNip("1231231233");
        contractor.setCompanyName("Company Name");
        contractor.setStreet("Street 22");
        contractor.setPostalCode("44-213");
        contractor.setCity("Cracow");

        return contractor;
    }
}
