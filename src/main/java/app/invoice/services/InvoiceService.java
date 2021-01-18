package app.invoice.services;

import app.invoice.models.Contractor;
import app.invoice.models.Invoice;
import app.invoice.models.PayingMethods;
import app.invoice.models.User;
import app.invoice.repositories.ContractorRepository;
import app.invoice.repositories.InvoiceRepository;
import app.invoice.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class InvoiceService {


    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final ContractorRepository contractorRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, UserRepository userRepository, ContractorRepository contractorRepository) {
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
        this.contractorRepository = contractorRepository;
    }

    public Invoice createInvoice(Invoice invoice, Long buyerId, Long dealerId) throws Exception {
        LocalDateTime localDateTime = LocalDateTime.now();
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        PayingMethods payingMethod = PayingMethods.valueOf(String.valueOf(invoice.getPayingMethod()));

        User foundUser = getUserById(dealerId);
        Contractor foundContractor = getContractorById(buyerId);

//        invoice.setBuyerId(buyerId);
//        invoice.setDealerId(dealerId);
        invoice.setPayingMethod(payingMethod);

        //tymczasowo ustawione defualtowo daty
        invoice.setDateOfIssue(date);
        invoice.setPaymentDate(date);
        invoice.setSeenDate(date);

        Invoice savedInvoice = invoiceRepository.save(invoice);
        //czy user, lub contractor ma miec liste faktur?

        return invoiceRepository.save(invoice);
    }

    public List<String> validateInvoice(Invoice invoice) {

        List<String> errors = new ArrayList<>();
        Set<ConstraintViolation<Invoice>> violations = validator.validate(invoice);

        if (!violations.isEmpty()) {
            violations.stream().forEach(err -> {
                errors.add(err.getMessage());
            });
            log.info("Error during validate invoice");
            return errors;
        }
        log.info("Invoice validation passed");
        return Arrays.asList();
    }

    public User getUserById(Long id) throws Exception {
        User foundUser = userRepository.getById(id);

        if (foundUser == null) {
            throw new Exception("User not found in database");
        }
        return foundUser;
    }

    public Contractor getContractorById(Long id) throws Exception {
        Contractor foundContractor = contractorRepository.getById(id);

        if (foundContractor == null) {
            throw new Exception("Contractor not found in database");
        }

        return foundContractor;
    }

    public Invoice editInvoice(Invoice invoice, Long id) throws Exception {
        Invoice savedInvoice = new Invoice();
        PayingMethods payingMethod = PayingMethods.valueOf(String.valueOf(invoice.getPayingMethod()));
        invoice.setPayingMethod(payingMethod);
        //możlwość edycji: invoiceNumber, city, payingMethod
        //możlwość edytowania dat do ustalenia
        try {
            Invoice foundInvoice = invoiceRepository.getById(id);
            if (foundInvoice == null) {
                throw new Exception("Invoice not found in database");
            }

            if (!foundInvoice.getInvoiceNumber().equals(invoice.getInvoiceNumber())) {
                foundInvoice.setInvoiceNumber(invoice.getInvoiceNumber());
                log.info("Invoice number changed");
            }
            if (!foundInvoice.getCity().equals(invoice.getCity())) {
                foundInvoice.setCity(invoice.getCity());
                log.info("City changed");
            }
            if (foundInvoice.getPayingMethod() != invoice.getPayingMethod()) {
                foundInvoice.setPayingMethod(invoice.getPayingMethod());
                log.info("Paying method changed");
            }

            validateInvoice(savedInvoice);

            savedInvoice = invoiceRepository.save(foundInvoice);
            log.info("Invoice updated!");
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }

        return savedInvoice;
    }

    public void deleteInvoice(Long invoiceId) throws Exception {
        Invoice invoice = invoiceRepository.getById(invoiceId);

        if (invoice == null) {
            throw new Exception("Invoice not found in database");
        }

        invoiceRepository.deleteById(invoice.getId());
    }
}
