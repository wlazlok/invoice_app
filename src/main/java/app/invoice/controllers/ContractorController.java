package app.invoice.controllers;

import app.invoice.models.Contractor;
import app.invoice.models.User;
import app.invoice.services.ContractorService;
import app.invoice.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            model.addAttribute("error", isValid);
            return "contractor/add-contractor";
        }
        try {
            User user = userService.getUserFromContext();
            contractorService.createContractor(contractor, user.getId());
        } catch (Exception e) {
            log.info("contractorController.catch.errors" + " " + e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("contractor", contractor);
            return "contractor/add-contractor";
        }
        log.info("contractorController.contractor.crated");
        return "redirect:/user/contractors";
    }

    @GetMapping("/edit/{contractorId}")
    public String getEditContractorView(@PathVariable("contractorId") String id, Model model) {
        Contractor contractor;
        try {
            contractor = contractorService.getContractorById(Long.valueOf(id));
        } catch (Exception e) {
            log.info("contractorController.edit.contractor.not.found " + e.getMessage());
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
            //todo wpisanie na fornt bledu
            log.info("contractorController.edit.catch.errror " + e.getMessage());
            model.addAttribute("contractor", contractor);
            return "contractor/add-contractor";
        }
        log.info("contractorController.edit.successfully");
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
        log.info("contractorController.delete.successfully");
        return "redirect:/user/contractors";
    }
}
