package app.invoice.services;

import app.invoice.models.Contractor;
import app.invoice.models.User;
import app.invoice.repositories.ContractorRepository;
import app.invoice.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
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
}
