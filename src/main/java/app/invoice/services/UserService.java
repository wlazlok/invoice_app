package app.invoice.services;

import app.invoice.models.ChangeUserPasswordForm;
import app.invoice.models.ResetPasswordForm;
import app.invoice.models.User;
import app.invoice.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.awt.print.PrinterJob;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

@Service
@Slf4j
public class UserService {

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    private final UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Qualifier("getJavaMailSender")
    @Autowired
    private JavaMailSender emailSender;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) throws Exception {
        Iterable<User> users = userRepository.findAll();
        List<String> userNames = new ArrayList<>();

        users.forEach(usr -> {
            userNames.add(usr.getUserName());
        });

        if (userNames.contains(user.getUserName())) {
            log.info("Username exists in DB");
            throw new Exception("User exists in DB");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<String> validateUser(User user) {
        List<String> errors = new ArrayList<>();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        if (!user.getPassword().equals(user.getConfirmPassword()) || violations.size() != 0 || !violations.isEmpty()) {
            errors.add("msg.passwords.are.not.the.same");
            violations.stream().forEach(err -> errors.add(err.getMessage()));
            log.info("Validation status: " + false);
            return errors;
        }
        log.info("Validation status: " + true);

        return Arrays.asList();
    }

    public List<String> changePassword(ChangeUserPasswordForm userForm) throws Exception {
        List<String> errors = new ArrayList<>();
        Set<ConstraintViolation<ChangeUserPasswordForm>> violations = validator.validate(userForm);

        if (!violations.isEmpty()) {
            violations.stream().forEach(err -> {
                errors.add(err.getMessage());
            });
            log.info("Change password finished with status: " + false);
            return errors;
        }

        User userFound = findUserByUserName(userForm.getUserName());
        boolean isOldPasswordEquals = bCryptPasswordEncoder.matches(userForm.getOldPassword(), userFound.getPassword());

        if (!isOldPasswordEquals) {
            throw new Exception("Old password ale not equals");
        }
        if (!userForm.getNewPassword().equals(userForm.getConfirmNewPassword())) {
            throw new Exception("New password are not the same!");
        }
        if (bCryptPasswordEncoder.matches(userForm.getNewPassword(), userFound.getPassword())) {
            throw new Exception("New and old password are equals");
        }

        userFound.setPassword(bCryptPasswordEncoder.encode(userForm.getNewPassword()));
        userFound.setConfirmPassword(userForm.getNewPassword());
        userRepository.save(userFound);
        return null;
    }

    public User findUserByUserName(String userName) throws Exception {
        User userFound = userRepository.getByUserName(userName);
        if(userFound != null) {
            log.info("User found in DB");
            return userFound;
        }
        log.info("User does not exists in DB: " + userName);
        throw new Exception("User not found");
    }

    public void sendEmailWithLinkAndResetPassword(ResetPasswordForm resetForm) throws Exception {

        User userFound = findUserByUserName(resetForm.getUserName());

        if (!resetForm.getEmail().equals(userFound.getEmail())) {
            log.info("Email not found!");
            throw new Exception("Email not found ");
        }
        String tmpPass = createTempPassword();

        userFound.setPassword(bCryptPasswordEncoder.encode(tmpPass));
        userFound.setConfirmPassword(tmpPass);
        userRepository.save(userFound);
        sendMessage(resetForm.getEmail(), tmpPass, userFound.getUserName());

        log.info("Email send");
    }

    public void sendMessage(String userEmail, String tmpPassword, String userName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("admin@invoice.com");
        message.setTo(userEmail);
        message.setSubject("Reset email link");
        message.setText("Your temporary passowrd: " + tmpPassword + "\nclick link to reset: " + createLink(tmpPassword, userName));
        emailSender.send(message);
    }

    public String createLink(String tempPass, String userName) {
        byte[] encodedUserName = Base64.getEncoder().encode(userName.getBytes());
        byte[] encodedTempPassword = Base64.getEncoder().encode(tempPass.getBytes());
        //todo change link when front ready
        return "http://localhost:8080/user/password/reset/?user="+new String(encodedUserName, StandardCharsets.UTF_8)+
                "&pass="+new String(encodedTempPassword, StandardCharsets.UTF_8);
    }

    public String createTempPassword() {
        return UUID.randomUUID().toString();
    }

    public List<String> validateUserModel(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        List<String> errors = Arrays.asList();

        if (!violations.isEmpty()) {
            violations.stream().forEach(err -> {
                errors.add(err.getMessage());
            });
            log.info("Validation finished with status: " + false);
            return errors;
        }

        return Arrays.asList();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
