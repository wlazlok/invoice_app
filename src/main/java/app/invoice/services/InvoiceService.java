package app.invoice.services;

import app.invoice.models.*;
import app.invoice.repositories.ContractorRepository;
import app.invoice.repositories.InvoicePositionRepository;
import app.invoice.repositories.InvoiceRepository;
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
public class InvoiceService {


    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final ContractorRepository contractorRepository;
    private final InvoicePositionRepository invoicePositionRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, UserRepository userRepository, ContractorRepository contractorRepository, InvoicePositionRepository invoicePositionRepository) {
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
        this.contractorRepository = contractorRepository;
        this.invoicePositionRepository = invoicePositionRepository;
    }

    public Invoice createInvoice(Invoice invoice, User user) {
        //todo dopisanie logiki walidacje itp ? moze do controllera? + lapanie bledow itp
        invoice.setContractor(contractorRepository.getById(invoice.getContractorId()));
        invoice.setUser(user);
        Invoice saved = invoiceRepository.save(invoice);
        invoice.getPosition().setGoodsAndServices(invoice.getGood());
        invoice.getPosition().setTotalPrice(invoice.getPosition().getGoodsAndServices().getPrice() * invoice.getPosition().getAmount());
        invoice.getPosition().setInvoice(saved);
        InvoicePositions savedPosition = invoicePositionRepository.save(invoice.getPosition());
        saved.getInvoicePositions().add(savedPosition);
        double suma = saved.getInvoicePositions().stream().mapToDouble(InvoicePositions::getTotalPrice).sum();
        saved.setTotalPrice(suma);
        return invoiceRepository.save(saved);
    }

    public Invoice updateInvoice(Long invoiceId, Invoice invoice) {
        Invoice foundInvoice = invoiceRepository.getById(Long.valueOf(invoiceId));
        invoice.getPosition().setGoodsAndServices(invoice.getGood());
        invoice.getPosition().setTotalPrice(invoice.getPosition().getGoodsAndServices().getPrice() * invoice.getPosition().getAmount());
        invoice.getPosition().setInvoice(foundInvoice);
        InvoicePositions savedPosition = invoicePositionRepository.save(invoice.getPosition());
        foundInvoice.getInvoicePositions().add(savedPosition);
        double suma = foundInvoice.getInvoicePositions().stream().mapToDouble(InvoicePositions::getTotalPrice).sum();
        foundInvoice.setTotalPrice(suma);
        return invoiceRepository.save(foundInvoice);
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

    public Invoice getInvoiceById(Long id) {
        //todo obsluga bledow itp
        return invoiceRepository.getById(id);
    }
}
