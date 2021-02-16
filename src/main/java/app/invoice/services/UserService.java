package app.invoice.services;

import app.invoice.configuration.ResourceBundleProperties;
import app.invoice.exceptions.IncorrectPassword;
import app.invoice.models.ChangeUserPasswordForm;
import app.invoice.models.Invoice;
import app.invoice.models.ResetPasswordForm;
import app.invoice.models.User;
import app.invoice.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static app.invoice.configuration.ResourceBundleProperties.*;

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

    public List<String> getAllUsernames() {
        Iterable<User> users = userRepository.findAll();
        List<String> usernames = new ArrayList<>();
        users.forEach(usr -> usernames.add(usr.getUsername()));
        return usernames;
    }

    public User createUser(User user) throws Exception {
        Iterable<User> users = userRepository.findAll();
        List<String> userNames = new ArrayList<>();

        users.forEach(usr -> {
            userNames.add(usr.getUsername());
        });

        if (userNames.contains(user.getUsername())) {
            log.info("Username exists in DB");
            throw new Exception("Username already exists!");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User editUser(User user, User dbUser) {
        boolean isPasswordEqual = bCryptPasswordEncoder.matches(user.getPassword(), dbUser.getPassword());
        if (!isPasswordEqual) {
            throw new IncorrectPassword("Niepoprawne hasło");
        }
        if (dbUser.getEmail() == null && user.getEmail() != null) {
            dbUser.setEmail(user.getEmail());
            log.info("Email changed");
        } else if (!dbUser.getEmail().equals(user.getEmail())) {
            dbUser.setEmail(user.getEmail());
            log.info("Email changed");
        }
        if (dbUser.getNip() == null && user.getNip() != null) {
            dbUser.setNip(user.getNip());
            log.info("Nip changed");
        } else if (!dbUser.getNip().equals(user.getNip())) {
            dbUser.setNip(user.getNip());
            log.info("Nip changed");
        }
        if (dbUser.getCompanyName() == null && user.getCompanyName() != null) {
            dbUser.setCompanyName(user.getCompanyName());
            log.info("Company name changed");
        } else if (!dbUser.getCompanyName().equals(user.getCompanyName())) {
            dbUser.setCompanyName(user.getCompanyName());
            log.info("Company name changed");
        }
        if (dbUser.getStreet() == null && user.getStreet() != null) {
            dbUser.setStreet(user.getStreet());
            log.info("Street changed");
        } else if (!dbUser.getStreet().equals(user.getStreet())) {
            dbUser.setStreet(user.getStreet());
            log.info("Street changed");
        }
        if (dbUser.getPostalCode() == null && user.getPostalCode() != null) {
            dbUser.setPostalCode(user.getPostalCode());
            log.info("Postal code changed");
        } else if (!dbUser.getPostalCode().equals(user.getPostalCode())) {
            dbUser.setPostalCode(user.getPostalCode());
            log.info("Postal code changed");
        }
        if (dbUser.getCity() == null && user.getCity() != null) {
            dbUser.setCity(user.getCity());
            log.info("City changed");
        } else if (!dbUser.getCity().equals(user.getCity())) {
            dbUser.setCity(user.getCity());
            log.info("City changed");
        }
        if (dbUser.getBankAccountNumber() == null && user.getBankAccountNumber() != null) {
            dbUser.setBankAccountNumber(user.getBankAccountNumber());
            log.info("Bank account number changed");
        } else if (!dbUser.getBankAccountNumber().equals(user.getBankAccountNumber())) {
            dbUser.setBankAccountNumber(user.getBankAccountNumber());
            log.info("Bank account number changed");
        }
        User savedUser = saveUser(dbUser);
        log.info("userService.edit.successfully");
        return savedUser;
    }

    public List<String> validateUser(User user) {
        ResourceBundle bundle = getBundleProperties();
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            log.info("Passwords are not the same");
            return Collections.singletonList("msg.passwords.are.not.the.same");
        }
        List<String> errors = new ArrayList<>();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        violations.forEach(err -> errors.add(bundle.getString(err.getMessage())));
        if (!violations.isEmpty()) {
            log.info("Validation status: " + false);
            return errors;

        }
        log.info("Validation status: " + true);

        return Collections.emptyList();
    }

    public List<String> changePassword(ChangeUserPasswordForm userForm, String password, String confirmPass) throws Exception {
        List<String> errors = new ArrayList<>();
        Set<ConstraintViolation<ChangeUserPasswordForm>> violations = validator.validate(userForm);
        userForm.setNewPassword(password);
        userForm.setConfirmNewPassword(confirmPass);
        if (!violations.isEmpty()) {
            violations.stream().forEach(err -> {
                System.out.println(err.getMessage());
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
        User userFound = userRepository.getByUsername(userName);
        if (userFound != null) {
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
        sendMessage(resetForm.getEmail(), tmpPass, userFound.getUsername());

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
        return "http://localhost:8080/reset/password?user=" + new String(encodedUserName, StandardCharsets.UTF_8) +
                "&pass=" + new String(encodedTempPassword, StandardCharsets.UTF_8);
    }

    public String createTempPassword() {
        return UUID.randomUUID().toString();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }


    public User getUserById(Long id) {
        return userRepository.getById(id);
    }

    public User getUserFromContext() {
        Long id;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            id = ((User) principal).getId();
            return getUserById(id);
        } else {
            String username = principal.toString();
            log.info("userService.getUserFromContext.userNotFound");
            return null;
        }
    }

    public List<Invoice> getAllInvoices() {
        return getUserFromContext().getInvoices();
    }
}
