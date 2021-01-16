package app.invoice.controllers;

import app.invoice.models.ChangeUserPasswordForm;
import app.invoice.models.ResetPasswordForm;
import app.invoice.models.User;
import app.invoice.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<List<String>> createUser(@RequestBody User user) {
        List<String> isValidated = userService.validateUser(user);
        if (isValidated.isEmpty()) {
            try {
                userService.createUser(user);
            } catch (Exception exception) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList(exception.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.OK).body(Arrays.asList("user created!"));
        }
        return ResponseEntity.ok(isValidated);
    }

    @PostMapping("/password/change")
    public ResponseEntity<List<String>> changePassword(@RequestBody ChangeUserPasswordForm changeUserPasswordForm) {
        List<String> error = new ArrayList<>();
        try {
            error = userService.changePassword(changeUserPasswordForm, null, null);
        } catch (Exception exception) {
            if (!error.isEmpty()) {
                log.info("Error during validation changing form");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }
            log.info("Error during changing password");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList(exception.getMessage()));
        }
        log.info("Password changed");
        return ResponseEntity.status(HttpStatus.OK).body(Arrays.asList());
    }

    @PostMapping("/password/reset/request")
    public ResponseEntity<String> sendEmailAndResetPassword(@RequestBody ResetPasswordForm resetForm) {
        try {
            userService.sendEmailWithLinkAndResetPassword(resetForm);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

        return ResponseEntity.ok("Email send to email: " + resetForm.getEmail());
    }

    @PostMapping("/password/reset")
    public ResponseEntity<List<String>> resetPassword(@RequestParam String user,
                                                      @RequestParam String pass,
                                                      @RequestBody ChangeUserPasswordForm changeForm) {
        List<String> error = new ArrayList<>();

        byte[] decodedUserByte = Base64.getDecoder().decode(user.getBytes());
        byte[] decodedPassByte = Base64.getDecoder().decode(pass.getBytes());

        String decodedUser = new String(decodedUserByte, StandardCharsets.UTF_8);
        String decodedPass = new String(decodedPassByte, StandardCharsets.UTF_8);

        if (!decodedUser.equals(changeForm.getUserName())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList("User names are not equals!"));
        }
        if (!decodedPass.equals(changeForm.getOldPassword())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList("Passowrd from link and form are not equals!"));
        }

        try {
            error = userService.changePassword(changeForm, null, null);
        } catch (Exception ex) {
            if (!error.isEmpty()) {
                log.info("Error during validation changing form");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }
            log.info("Error during changing password");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList(ex.getMessage()));
        }
        log.info("Password changed");
        return ResponseEntity.ok(Arrays.asList("Password changed"));
    }

    @PostMapping("/edit")
    public ResponseEntity<List<String>> editUser(@RequestBody User user) {
        List<String> errors = Arrays.asList();
        try {
            User userFromDb = userService.findUserByUserName(user.getUsername());

            // możliwość zmiany: email, nip, companyName, street, postalCode, city, bankAccount, hasła

            if (!userFromDb.getEmail().equals(user.getEmail())) {
                userFromDb.setEmail(user.getEmail());
                log.info("Email changed");
            }
            if (!userFromDb.getNip().equals(user.getNip())) {
                userFromDb.setNip(user.getNip());
                log.info("Nip changed");
            }
            if (!userFromDb.getCompanyName().equals(user.getCompanyName())) {
                userFromDb.setCompanyName(user.getCompanyName());
                log.info("Company name changed");
            }
            if (!userFromDb.getStreet().equals(user.getStreet())) {
                userFromDb.setStreet(user.getStreet());
                log.info("Street changed");
            }
            if (!userFromDb.getPostalCode().equals(user.getPostalCode())) {
                userFromDb.setPostalCode(user.getPostalCode());
                log.info("Postal code changed");
            }
            if (!userFromDb.getCity().equals(user.getCity())) {
                userFromDb.setCity(user.getCity());
                log.info("City changed");
            }
            if (!userFromDb.getBankAccountNumber().equals(user.getBankAccountNumber())) {
                userFromDb.setBankAccountNumber(user.getBankAccountNumber());
                log.info("Bank acc number changed");
            }

            boolean isOldPasswordEquals = bCryptPasswordEncoder.matches(user.getPassword(), userFromDb.getPassword());

            //todo chyabto trzeba zrobić inaczej
            if (!isOldPasswordEquals) {
                System.out.println(user.getPassword() + " " + user.getConfirmPassword());
                if (user.getPassword().equals(user.getConfirmPassword())) {
                    userFromDb.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                    userFromDb.setConfirmPassword(user.getConfirmPassword());
                    log.info("Password changed");
                } else {
                    throw new Exception("Password are not equals");
                }
            } else {
                //user need to confirm change by typing password
                if (!bCryptPasswordEncoder.matches(user.getPassword(), userFromDb.getPassword())) {
                throw new Exception("Bad password!");
                }
                userFromDb.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                userFromDb.setConfirmPassword(user.getPassword());
            }
            errors = userService.validateUserModel(user);

            if (!errors.isEmpty()) {
                throw new Exception("User not valid!");
            }

            userService.saveUser(userFromDb);
            log.info("User saved in DB");
        } catch (Exception ex) {
            if (!errors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList(ex.getMessage()));
        }

        return ResponseEntity.ok(Arrays.asList("User updated successful"));
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam("userName") String userName) {

        try {
            userService.deleteUser(userName);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

        return ResponseEntity.ok("User deleted");
    }
}
