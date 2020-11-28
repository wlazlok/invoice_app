package app.invoice.controllers;

import app.invoice.models.Contractor;
import app.invoice.services.ContractorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/contractor")
public class ContractorController {

    private final ContractorService contractorService;

    public ContractorController(ContractorService contractorService) {
        this.contractorService = contractorService;
    }

    @PostMapping("/create")
    public ResponseEntity<List<String>> createContractor(@RequestBody Contractor contractor,
                                                         @RequestParam Long userId) {

        List<String> isValidated = contractorService.validateContractor(contractor);
        if (!isValidated.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(isValidated);
        }
        try {
            contractorService.createContractor(contractor, userId);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList(ex.getMessage()));
        }

        return ResponseEntity.ok(Arrays.asList("User created"));
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteContractor(@RequestParam("id") String id) {

        try {
            contractorService.deleteContractor(Long.valueOf(id));
        } catch (Exception ex) {
            log.info("Error during deleting: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

        log.info("Contractor deleted");
        return ResponseEntity.ok("Contractor deleted");
    }

    @PostMapping("/edit")
    public ResponseEntity<List<String>> editContractor(@RequestBody Contractor contractor,
                                                       @RequestParam("id") String id) {

        //możliwość edycji: email, nip, companyName, street, postalCode, city
        try {
            contractorService.editContractor(contractor, Long.valueOf(id));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList(ex.getMessage()));
        }
        return ResponseEntity.ok(Arrays.asList("Contractor updated successful"));
    }
}
