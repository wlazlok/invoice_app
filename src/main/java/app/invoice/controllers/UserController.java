package app.invoice.controllers;

import app.invoice.exceptions.IncorrectPassword;
import app.invoice.models.User;
import app.invoice.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;

    @GetMapping("/contractors")
    public String getContractorsView(Model model) {
        User user = userService.getUserFromContext();
        model.addAttribute("contractors", user.getContractors());

        return "contractor/contractors";
    }

    @GetMapping("/goods")
    public String getGoodsAndServices(Model model) {
        User user = userService.getUserFromContext();
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
        User dbUser = userService.getUserFromContext();
        try {
            //walidacja
            user.setUsername(dbUser.getUsername());
            user.setConfirmPassword(user.getPassword());
            List<String> errors = userService.validateUser(user);
            if (!errors.isEmpty()) {
                log.info("usercontroller.validate.user.errors");
                model.addAttribute("user", dbUser);
                model.addAttribute("error", errors);
                return "user/edit-user";
            }
            //edycja oraz zapis zmian
            log.info("usercontroller.user.updated.succesfully");
            User savedUser = userService.editUser(user, dbUser);
            model.addAttribute("error", "Dane zostały zaktualizowane");
            model.addAttribute("user", savedUser);
            return "user/edit-user";
        } catch (IncorrectPassword ex) {
            log.info("user.controller.incorrect.confirm.password");
            model.addAttribute("user", dbUser);
            model.addAttribute("error", ex.getMessage());
            return "user/edit-user";
        } catch (Exception e) {
            log.info("usercontroller.update.catch.errors " + e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", dbUser);
            return "user/edit-user";
        }
    }
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
}
