package app.invoice.services;

import app.invoice.models.Contractor;
import app.invoice.models.User;
import app.invoice.repositories.ContractorRepository;
import app.invoice.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

@Slf4j
@Service
public class ContractorService {

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    private final ContractorRepository contractorRepository;
    private final UserRepository userRepository;

    public ContractorService(ContractorRepository contractorRepository, UserRepository userRepository) {
        this.contractorRepository = contractorRepository;
        this.userRepository = userRepository;
    }

    public Contractor createContractor(Contractor contractor, Long userId) {

        User foundUser = userRepository.getById(userId);
        contractor.setUser(foundUser);

        Contractor contractorSaved = contractorRepository.save(contractor);
        foundUser.getContractors().add(contractorSaved);

        userRepository.save(foundUser);

        return contractorSaved;
    }

    public List<String> validateContractor(Contractor contractor) {
        List<String> errors = new ArrayList<>();
        Set<ConstraintViolation<Contractor>> violations = validator.validate(contractor);

        if (!violations.isEmpty()) {
            violations.stream().forEach(err -> {
                errors.add(err.getMessage());
            });
            log.info("Error during validate contractor");
            return errors;
        }
        log.info("Contractor validation passed");
        return Arrays.asList();
    }

    public void deleteContractor(Long contractorId) throws Exception {

        Contractor contractorFound = contractorRepository.getById(contractorId);

        if (contractorFound == null) {
            throw new Exception("Contractor not found in database!");
        }

        contractorRepository.deleteById(contractorFound.getId());
    }

    public Contractor editContractor(Contractor contractor, Long id) throws Exception {
        Contractor savedContractor;
        try {
            Contractor foundContractor = getContractorById(id);

            if (!foundContractor.getEmail().equals(contractor.getEmail())) {
                foundContractor.setEmail(contractor.getEmail());
                log.info("Email changed");
            }
            if (!foundContractor.getNip().equals(contractor.getNip())) {
                foundContractor.setNip(contractor.getNip());
                log.info("Nip changed");
            }
            if (!foundContractor.getCompanyName().equals(contractor.getCompanyName())) {
                foundContractor.setCompanyName(contractor.getCompanyName());
                log.info("Company name changed");
            }
            if (!foundContractor.getStreet().equals(contractor.getStreet())) {
                foundContractor.setStreet(contractor.getStreet());
                log.info("Street changed");
            }
            if (!foundContractor.getPostalCode().equals(contractor.getPostalCode())) {
                foundContractor.setPostalCode(contractor.getPostalCode());
                log.info("Postal code changed");
            }
            if (!foundContractor.getCity().equals(contractor.getCity())) {
                foundContractor.setCity(contractor.getCity());
                log.info("City changed");
            }

            savedContractor = contractorRepository.save(foundContractor);
            log.info("Contractor saved!");

        } catch (Exception ex) {
           throw new Exception(ex.getMessage());
        }
        return savedContractor;
    }

    public Contractor getContractorById(Long id) throws Exception {
        Contractor contractorFound = contractorRepository.getById(id);

        if (contractorFound == null) {
            throw new Exception("Contractor not found in database!");
        }
        return contractorFound;
    }
}
