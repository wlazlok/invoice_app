package app.invoice;

import app.invoice.models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidationTest {

    List<String> validationErrors = new ArrayList<>();
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @AfterEach
    void clearList() {
        validationErrors.clear();
    }

    @Disabled
    @Test
    void ChangeUserPasswordFormWithNullAllFieldsTest() {
        List<String> errors = Arrays.asList("ChangeUserPasswordForm new password is blank",
                "ChangeUserPasswordForm username is blank",
                "ChangeUserPasswordForm confirm password is blank",
                "ChangeUserPasswordForm old password is blank");

        ChangeUserPasswordForm changeUserPasswordForm = BuildModels.changeUserPasswordFormWithAllFieldsNull();
        Set<ConstraintViolation<ChangeUserPasswordForm>> violations = validator.validate(changeUserPasswordForm);
        violations.stream().forEach(err -> {
            validationErrors.add(err.getMessage());
        });

        Collections.sort(errors);
        Collections.sort(validationErrors);

        assertEquals(errors, validationErrors);
    }
    @Disabled
    @Test
    void ContractorWithNullAllFieldsTest() {
        List<String> errors = Arrays.asList("Contractor postal code is blank",
                "Contractor company name is blank",
                "Contractor city is blank",
                "Contractor NIP is blank",
                "Contractor street is blank",
                "Contractor email is blank");

        Contractor contractor = BuildModels.contractorWithNullAlFields();
        Set<ConstraintViolation<Contractor>> violations = validator.validate(contractor);
        violations.stream().forEach(err -> {
            validationErrors.add(err.getMessage());
        });

        Collections.sort(errors);
        Collections.sort(validationErrors);

        assertEquals(errors, validationErrors);
    }
    @Disabled
    @Test
    void GoodsAndServicesWithNullAllFieldsTest() {
        List<String> errors = Arrays.asList("GoodsAndServices name is null",
                "GoodsAndServices price is blank",
                "GoodsAndServices unit is blank");

        GoodsAndServices goodsAndServices = BuildModels.goodsAndServicesWithNullAllFields();
        Set<ConstraintViolation<GoodsAndServices>> violations = validator.validate(goodsAndServices);
        violations.stream().forEach(err -> {
            validationErrors.add(err.getMessage());
        });

        Collections.sort(errors);
        Collections.sort(validationErrors);

        assertEquals(errors, validationErrors);
    }
    @Disabled
    @Test
    void InvoiceWithNullAllFieldsTest() {
        List<String> errors = Arrays.asList("Invoice city is blank",
                "Invoice invoice number is blank",
                "Invoice paying method is blank");

        Invoice invoice = BuildModels.InvoiceWithNullAllFields();
        Set<ConstraintViolation<Invoice>> violations = validator.validate(invoice);
        violations.stream().forEach(err -> {
            validationErrors.add(err.getMessage());
        });

        Collections.sort(errors);
        Collections.sort(validationErrors);

        assertEquals(errors, validationErrors);
    }
    @Disabled
    @Test
    void ResetPasswordFormWithNullAllFieldsTest() {
        List<String> errors = Arrays.asList("ResetPasswordForm email is blank",
                "ResetPasswordForm user name is blank");

        ResetPasswordForm resetPasswordForm = BuildModels.resetPasswordFormWithNullAllFields();
        Set<ConstraintViolation<ResetPasswordForm>> violations = validator.validate(resetPasswordForm);
        violations.stream().forEach(err -> {
            validationErrors.add(err.getMessage());
        });

        Collections.sort(errors);
        Collections.sort(validationErrors);

        assertEquals(errors, validationErrors);
    }
    @Disabled
    @Test
    void UserWithNullAllFields() {
        List<String> errors = Arrays.asList("User NIP is blank",
                "User bank ccount number is blank",
                "User city is blank",
                "User company name is blank",
                "User email is blank",
                "User password is blank",
                "User postal code is blank",
                "User street is blank",
                "User username is null",
                "user.confirmpassword.is.null");

        User user = BuildModels.UserWithNullAllFields();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        violations.stream().forEach(err -> {
            validationErrors.add(err.getMessage());
        });

        Collections.sort(errors);
        Collections.sort(validationErrors);

        assertEquals(errors, validationErrors);
    }
}
