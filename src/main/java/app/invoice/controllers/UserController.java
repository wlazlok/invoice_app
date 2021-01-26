package app.invoice.controllers;

import app.invoice.models.User;
import app.invoice.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/contractors")
    public String getContractorsView(Model model) {
        User user = userService.getUserFromContext(); //todo security tylko zalogowani
        model.addAttribute("contractors", user.getContractors());

        return "contractor/contractors";
    }

    @GetMapping("/goods")
    public String getGoodsAndServices(Model model) {
        User user = userService.getUserFromContext(); //todo security tylko zalogowani
        model.addAttribute("goods", user.getGoodsAndServices());

        return "goods/goods-services";
    }

    @GetMapping("/edit")
    public String getEditUserView(Model model) {
        User user;
        try {
            user = userService.getUserFromContext();
        } catch (Exception e) {
            log.info("userController.get.user.catch.errors " + e.getMessage());
            return null; //todo cos tu trzeba dac innego i opisac blad
        }
        log.info("userController.get.user.succesfull");
        model.addAttribute("user", user);
        return "user/edit-user";
    }

    @Transactional
    @PostMapping("/edit")
    public String editUser(@ModelAttribute("user") User user, Model model) {
        User userFound = userService.getUserFromContext();
        user.setUsername(userFound.getUsername());
        user.setConfirmPassword(user.getPassword());
        List<String> errors = userService.validateUser(user);
        if (!errors.isEmpty()) {
            log.info("usercontroller.validate.user.errros ");
            errors.forEach(System.out::println);
            model.addAttribute("user", user);
            //todo zwrocic jakie sa bledy
            return "user/edit-user";
        }

        try {
            // możliwość zmiany: email, nip, companyName, street, postalCode, city, bankAccount
            //todo haslo trzbea dorobic (zmiane)
            //todo dane uzytkownika na pocatku sa nullami -> w ifach zrobic jakies warunki
            if (userFound.getEmail() == null && user.getEmail() != null) {
                userFound.setEmail(user.getEmail());
                log.info("Email changed");
            } else if (!userFound.getEmail().equals(user.getEmail())) {
                userFound.setEmail(user.getEmail());
                log.info("Email changed");
            }
            if (userFound.getNip() == null && user.getNip() != null) {
                userFound.setNip(user.getNip());
                log.info("Nip changed");
            } else if (!userFound.getNip().equals(user.getNip())) {
                userFound.setNip(user.getNip());
                log.info("Nip changed");
            }
            if (userFound.getCompanyName() == null && user.getCompanyName() != null) {
                userFound.setCompanyName(user.getCompanyName());
                log.info("Company name changed");
            } else if (!userFound.getCompanyName().equals(user.getCompanyName())) {
                userFound.setCompanyName(user.getCompanyName());
                log.info("Company name changed");
            }
            if (userFound.getStreet() == null && user.getStreet() != null) {
                userFound.setStreet(user.getStreet());
                log.info("Street changed");
            } else if (!userFound.getStreet().equals(user.getStreet())) {
                userFound.setStreet(user.getStreet());
                log.info("Street changed");
            }
            if (userFound.getPostalCode() == null && user.getPostalCode() != null) {
                userFound.setPostalCode(user.getPostalCode());
                log.info("Postal code changed");
            } else if (!userFound.getPostalCode().equals(user.getPostalCode())) {
                userFound.setPostalCode(user.getPostalCode());
                log.info("Postal code changed");
            }
            if (userFound.getCity() == null && user.getCity() != null) {
                userFound.setCity(user.getCity());
                log.info("City changed");
            } else if (!userFound.getCity().equals(user.getCity())) {
                userFound.setCity(user.getCity());
                log.info("City changed");
            }
            if (userFound.getBankAccountNumber() == null && user.getBankAccountNumber() != null) {
                userFound.setBankAccountNumber(user.getBankAccountNumber());
                log.info("Bank acc number changed");
            } else if (!userFound.getBankAccountNumber().equals(user.getBankAccountNumber())) {
                userFound.setBankAccountNumber(user.getBankAccountNumber());
                log.info("Bank acc number changed");
            }
            System.out.println("zapisuje");
            User savedUser = userService.saveUser(userFound);
            log.info("usercontroller.user.updated.succesfully");
            model.addAttribute("user", savedUser);
            return "user/edit-user";
        } catch (Exception e) {
            log.info("usercontroller.update.catch.errors " + e.getMessage());
            //todo na front errory o bledach itp
            model.addAttribute("user", userFound);
            return "user/edit-user";
        }
    }
//    @PostMapping("/edit")
//    public ResponseEntity<List<String>> editUser(@RequestBody User user) {
//        List<String> errors = Arrays.asList();
//        try {
//            User userFromDb = userService.findUserByUserName(user.getUsername());
//
//            // możliwość zmiany: email, nip, companyName, street, postalCode, city, bankAccount, hasła
//
//            if (!userFromDb.getEmail().equals(user.getEmail())) {
//                userFromDb.setEmail(user.getEmail());
//                log.info("Email changed");
//            }
//            if (!userFromDb.getNip().equals(user.getNip())) {
//                userFromDb.setNip(user.getNip());
//                log.info("Nip changed");
//            }
//            if (!userFromDb.getCompanyName().equals(user.getCompanyName())) {
//                userFromDb.setCompanyName(user.getCompanyName());
//                log.info("Company name changed");
//            }
//            if (!userFromDb.getStreet().equals(user.getStreet())) {
//                userFromDb.setStreet(user.getStreet());
//                log.info("Street changed");
//            }
//            if (!userFromDb.getPostalCode().equals(user.getPostalCode())) {
//                userFromDb.setPostalCode(user.getPostalCode());
//                log.info("Postal code changed");
//            }
//            if (!userFromDb.getCity().equals(user.getCity())) {
//                userFromDb.setCity(user.getCity());
//                log.info("City changed");
//            }
//            if (!userFromDb.getBankAccountNumber().equals(user.getBankAccountNumber())) {
//                userFromDb.setBankAccountNumber(user.getBankAccountNumber());
//                log.info("Bank acc number changed");
//            }
//
//            boolean isOldPasswordEquals = bCryptPasswordEncoder.matches(user.getPassword(), userFromDb.getPassword());
//
//            //todo chyabto trzeba zrobić inaczej
//            if (!isOldPasswordEquals) {
//                System.out.println(user.getPassword() + " " + user.getConfirmPassword());
//                if (user.getPassword().equals(user.getConfirmPassword())) {
//                    userFromDb.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//                    userFromDb.setConfirmPassword(user.getConfirmPassword());
//                    log.info("Password changed");
//                } else {
//                    throw new Exception("Password are not equals");
//                }
//            } else {
//                //user need to confirm change by typing password
//                if (!bCryptPasswordEncoder.matches(user.getPassword(), userFromDb.getPassword())) {
//                throw new Exception("Bad password!");
//                }
//                userFromDb.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//                userFromDb.setConfirmPassword(user.getPassword());
//            }
//            errors = userService.validateUserModel(user);
//
//            if (!errors.isEmpty()) {
//                throw new Exception("User not valid!");
//            }
//
//            userService.saveUser(userFromDb);
//            log.info("User saved in DB");
//        } catch (Exception ex) {
//            if (!errors.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
//            }
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList(ex.getMessage()));
//        }
//
//        return ResponseEntity.ok(Arrays.asList("User updated successful"));
//    }


//    @PostMapping("/create")
//    public ResponseEntity<List<String>> createUser(@RequestBody User user) {
//        List<String> isValidated = userService.validateUser(user);
//        if (isValidated.isEmpty()) {
//            try {
//                userService.createUser(user);
//            } catch (Exception exception) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList(exception.getMessage()));
//            }
//            return ResponseEntity.status(HttpStatus.OK).body(Arrays.asList("user created!"));
//        }
//        return ResponseEntity.ok(isValidated);
//    }
//
//    @PostMapping("/password/change")
//    public ResponseEntity<List<String>> changePassword(@RequestBody ChangeUserPasswordForm changeUserPasswordForm) {
//        List<String> error = new ArrayList<>();
//        try {
//            error = userService.changePassword(changeUserPasswordForm, null, null);
//        } catch (Exception exception) {
//            if (!error.isEmpty()) {
//                log.info("Error during validation changing form");
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//            }
//            log.info("Error during changing password");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList(exception.getMessage()));
//        }
//        log.info("Password changed");
//        return ResponseEntity.status(HttpStatus.OK).body(Arrays.asList());
//    }
//
//    @PostMapping("/password/reset/request")
//    public ResponseEntity<String> sendEmailAndResetPassword(@RequestBody ResetPasswordForm resetForm) {
//        try {
//            userService.sendEmailWithLinkAndResetPassword(resetForm);
//        } catch (Exception ex) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
//        }
//
//        return ResponseEntity.ok("Email send to email: " + resetForm.getEmail());
//    }
//
//    @PostMapping("/password/reset")
//    public ResponseEntity<List<String>> resetPassword(@RequestParam String user,
//                                                      @RequestParam String pass,
//                                                      @RequestBody ChangeUserPasswordForm changeForm) {
//        List<String> error = new ArrayList<>();
//
//        byte[] decodedUserByte = Base64.getDecoder().decode(user.getBytes());
//        byte[] decodedPassByte = Base64.getDecoder().decode(pass.getBytes());
//
//        String decodedUser = new String(decodedUserByte, StandardCharsets.UTF_8);
//        String decodedPass = new String(decodedPassByte, StandardCharsets.UTF_8);
//
//        if (!decodedUser.equals(changeForm.getUserName())) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList("User names are not equals!"));
//        }
//        if (!decodedPass.equals(changeForm.getOldPassword())) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList("Passowrd from link and form are not equals!"));
//        }
//
//        try {
//            error = userService.changePassword(changeForm, null, null);
//        } catch (Exception ex) {
//            if (!error.isEmpty()) {
//                log.info("Error during validation changing form");
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//            }
//            log.info("Error during changing password");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList(ex.getMessage()));
//        }
//        log.info("Password changed");
//        return ResponseEntity.ok(Arrays.asList("Password changed"));
//    }
//
//
//    @PostMapping("/delete")
//    public ResponseEntity<String> deleteUser(@RequestParam("userName") String userName) {
//
//        try {
//            userService.deleteUser(userName);
//        } catch (Exception ex) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
//        }
//
//        return ResponseEntity.ok("User deleted");
//    }
}
