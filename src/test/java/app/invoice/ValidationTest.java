package app.invoice;

import app.invoice.models.*;
import org.junit.jupiter.api.AfterEach;
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

    @Test
    void ChangeUserPasswordFormWithNullAllFieldsTest() {
        List<String> errors = Arrays.asList("ChangeUserPasswordForm old password is blank",
                "ChangeUserPasswordForm username is blank",
                "ChangeUserPasswordForm username is blank");

        ChangeUserPasswordForm changeUserPasswordForm = BuildModels.changeUserPasswordFormWithAllFieldsNull();
        Set<ConstraintViolation<ChangeUserPasswordForm>> violations = validator.validate(changeUserPasswordForm);
        violations.stream().forEach(err -> {
            validationErrors.add(err.getMessage());
        });

        Collections.sort(errors);
        Collections.sort(validationErrors);

        assertEquals(errors, validationErrors);
    }

    @Test
    void ContractorWithNullAllFieldsTest() {
        List<String> errors = Arrays.asList("Pole NIP nie może być puste",
                "Pole email nie może być puste",
                "Pole kod pocztowy nie może być puste",
                "Pole miasto nie może być puste",
                "Pole nazwa firmy nie może być puste",
                "Pole ulica nie może być puste");

        Contractor contractor = BuildModels.contractorWithNullAlFields();
        Set<ConstraintViolation<Contractor>> violations = validator.validate(contractor);
        violations.stream().forEach(err -> {
            validationErrors.add(err.getMessage());
        });

        Collections.sort(errors);
        Collections.sort(validationErrors);

        assertEquals(errors, validationErrors);
    }

    @Test
    void GoodsAndServicesWithNullAllFieldsTest() {
        List<String> errors = Arrays.asList("Prosze podać jednostkę",
                "Proszę podać cenę",
                "Proszę podać nazwę");

        GoodsAndServices goodsAndServices = BuildModels.goodsAndServicesWithNullAllFields();
        Set<ConstraintViolation<GoodsAndServices>> violations = validator.validate(goodsAndServices);
        violations.stream().forEach(err -> {
            validationErrors.add(err.getMessage());
        });

        Collections.sort(errors);
        Collections.sort(validationErrors);

        assertEquals(errors, validationErrors);
    }

    @Test
    void InvoiceWithNullAllFieldsTest() {
        List<String> errors = Arrays.asList("Proszę podać datę płatności",
                "Proszę podać datę sprzedaży",
                "Proszę podać datę wystawienia",
                "Proszę podać miejsce wystawienia",
                "Proszę podać numer faktury",
                "Proszę wybrać sposób płatności");

        Invoice invoice = BuildModels.InvoiceWithNullAllFields();
        Set<ConstraintViolation<Invoice>> violations = validator.validate(invoice);
        violations.stream().forEach(err -> {
            validationErrors.add(err.getMessage());
        });

        Collections.sort(errors);
        Collections.sort(validationErrors);

        assertEquals(errors, validationErrors);
    }

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

    @Test
    void UserWithNullAllFields() {
        List<String> errors = Arrays.asList("msg.err.user.email.is.null",
                "msg.err.user.password.is.empty",
                "msg.err.user.password.is.empty",
                "msg.err.user.username.is.empty" ,
                "msg.err.user.username.is.empty");

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
