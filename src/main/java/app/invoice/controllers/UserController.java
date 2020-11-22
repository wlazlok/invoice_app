package app.invoice.controllers;

import app.invoice.models.ChangeUserPasswordForm;
import app.invoice.models.ResetPasswordForm;
import app.invoice.models.User;
import app.invoice.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            error = userService.changePassword(changeUserPasswordForm);
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
            error = userService.changePassword(changeForm);
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
}
