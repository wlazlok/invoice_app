package app.invoice.controllers;

import app.invoice.models.Contractor;
import app.invoice.models.User;
import app.invoice.services.ContractorService;
import app.invoice.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@Controller
@RequestMapping("/contractor")
public class ContractorController {

    private final ContractorService contractorService;
    private final UserService userService;

    public ContractorController(ContractorService contractorService, UserService userService) {
        this.contractorService = contractorService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String getCreateContractorView(Model model) {

        model.addAttribute("contractor", new Contractor());
        return "contractor/add-contractor";
    }

    @PostMapping("/create")
    public String createContractor(@ModelAttribute("contractor") Contractor contractor, Model model) {
        List<String> isValid = contractorService.validateContractor(contractor);
        if (!isValid.isEmpty()) {
            //todo dodac błedy walidacji na front
            return "contractor/add-contractor";
        }
        try {
            User user = userService.getUserFromContext();
            contractorService.createContractor(contractor, user.getId());
        } catch (Exception e) {
            //todo bledy z e.getMessage na front
            log.info("contractorController.catch.errors" + " " + e.getMessage());
        }
        log.info("contractorController.contractor.crated");
        //todo inne przekierowanie
        return "redirect:/user/contractors";
    }

    @GetMapping("/edit/{contractorId}")
    public String getEditContractorView(@PathVariable("contractorId") String id, Model model) {
        Contractor contractor;
        try {
            contractor = contractorService.getContractorById(Long.valueOf(id));
        } catch (Exception e) {
            log.info("contractorController.edit.contractor.not.found " + e.getMessage());
            //todo moze byc przekierowanie na liste kntrahentow + tam error ze nie ma takiego
            return "redirect:/user/contractors";
        }
        model.addAttribute("contractor", contractor);
        return "contractor/add-contractor";
    }

    @PostMapping("/edit/{contractorId}")
    public String editContractor(@PathVariable("contractorId") String id, @ModelAttribute("contractor") Contractor contractor ,Model model) {

        try {
            contractorService.editContractor(contractor, Long.valueOf(id));
        } catch (Exception e) {
            //todo wpisanie nafrnt wyajtku
            log.info("contractorController.edit.catch.errror " + e.getMessage());
            model.addAttribute("contractor", contractor);
            return "contractor/add-contractor";
        }
        log.info("contractorController.edit.succesfull");
        return "redirect:/user/contractors";
    }

    @GetMapping("/delete/{contractorId}")
    public String deleteContractor(@PathVariable("contractorId") String id) {
        try {
            contractorService.deleteContractor(Long.valueOf(id));
        } catch (Exception e) {
            //todo dodac blad i wyswetlic na stronce
            log.info("contractorController.delete.catch.error " + e.getMessage());
            return "redirect:/user/contractors";
        }
        log.info("contractorController.delete.succesfull");
        //todo wypisanie komunikatu o sukcesie
        return "redirect:/user/contractors";
    }

//    @PostMapping("/delete")
//    public ResponseEntity<String> deleteContractor(@RequestParam("id") String id) {
//
//        try {
//            contractorService.deleteContractor(Long.valueOf(id));
//        } catch (Exception ex) {
//            log.info("Error during deleting: " + ex.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
//        }
//
//        log.info("Contractor deleted");
//        return ResponseEntity.ok("Contractor deleted");
//    }

    //
//    @PostMapping("/edit")
//    public ResponseEntity<List<String>> editContractor(@RequestBody Contractor contractor,
//                                                       @RequestParam("id") String id) {
//
//        //możliwość edycji: email, nip, companyName, street, postalCode, city
//        try {
//            contractorService.editContractor(contractor, Long.valueOf(id));
//        } catch (Exception ex) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList(ex.getMessage()));
//        }
//        return ResponseEntity.ok(Arrays.asList("Contractor updated successful"));
//    }

//    @PostMapping("/create")
//    public ResponseEntity<List<String>> createContractor(@RequestBody Contractor contractor,
//                                                         @RequestParam Long userId) {
//
//        List<String> isValidated = contractorService.validateContractor(contractor);
//        if (!isValidated.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(isValidated);
//        }
//        try {
//            contractorService.createContractor(contractor, userId);
//        } catch (Exception ex) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList(ex.getMessage()));
//        }
//
//        return ResponseEntity.ok(Arrays.asList("User created"));
//    }
//
//    @PostMapping("/delete")
//    public ResponseEntity<String> deleteContractor(@RequestParam("id") String id) {
//
//        try {
//            contractorService.deleteContractor(Long.valueOf(id));
//        } catch (Exception ex) {
//            log.info("Error during deleting: " + ex.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
//        }
//
//        log.info("Contractor deleted");
//        return ResponseEntity.ok("Contractor deleted");
//    }
}
