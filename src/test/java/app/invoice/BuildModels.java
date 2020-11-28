package app.invoice;

import app.invoice.models.*;

public class BuildModels {

    public static ChangeUserPasswordForm changeUserPasswordFormWithAllFieldsNull() {
        return ChangeUserPasswordForm.builder()
                .userName(null)
                .oldPassword(null)
                .newPassword(null)
                .confirmNewPassword(null)
                .build();
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
        invoice.setDealerId(null);
        invoice.setBuyerId(null);
        invoice.setSeenDate(null);

        return invoice;
    }

    public static ResetPasswordForm resetPasswordFormWithNullAllFields() {
        return ResetPasswordForm.builder()
                .userName(null)
                .email(null)
                .build();
    }

    public static User UserWithNullAllFields() {
        User user = new User();
        user.setId(null);
        user.setUserName(null);
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
}
