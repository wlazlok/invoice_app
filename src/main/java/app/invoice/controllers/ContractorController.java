package app.invoice.controllers;

import app.invoice.models.Contractor;
import app.invoice.services.ContractorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
}
