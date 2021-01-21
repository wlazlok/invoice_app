package app.invoice.services;

import app.invoice.models.GoodsAndServices;
import app.invoice.models.Invoice;
import app.invoice.models.InvoicePositions;
import app.invoice.repositories.GoodsAndServicesRepository;
import app.invoice.repositories.InvoicePositionRepository;
import app.invoice.repositories.InvoiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class InvoicePositionService {

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    private final InvoicePositionRepository invoicePositionRepository;
    private final GoodsAndServicesRepository goodsAndServicesRepository;
    private final InvoiceRepository invoiceRepository;

    public InvoicePositionService(InvoicePositionRepository invoicePositionRepository, GoodsAndServicesRepository goodsAndServicesRepository, InvoiceRepository invoiceRepository) {
        this.invoicePositionRepository = invoicePositionRepository;
        this.goodsAndServicesRepository = goodsAndServicesRepository;
        this.invoiceRepository = invoiceRepository;
    }


    public InvoicePositions createInvoicePosition(InvoicePositions invoicePositions, Long goodsAndServiceId, Long invoiceId) throws Exception {
        GoodsAndServices foundGoodsAndServices = findGoodsAndServices(goodsAndServiceId);
        Invoice foundInvoice = findInvoice(invoiceId);

        Double totalPrice = invoicePositions.getAmount() * foundGoodsAndServices.getPrice();

//        invoicePositions.setInvoiceId(foundInvoice.getId());
        invoicePositions.setGoodsAndServicesId(foundGoodsAndServices.getId());
        invoicePositions.setTotalPrice(totalPrice);

        //czy invoice positions gdzies zapisuje do uzytkowników, faktury albo contractor?
        validateInvoicePositions(invoicePositions);

        return invoicePositionRepository.save(invoicePositions);
    }

    public GoodsAndServices findGoodsAndServices(Long id) throws Exception {
        GoodsAndServices goodsAndServices = goodsAndServicesRepository.getById(id);

        if (goodsAndServices == null) {
            throw new Exception("GoodsAndServices not found in database");
        }

        return goodsAndServices;
    }

    public Invoice findInvoice(Long id) throws Exception {
        Invoice invoice = invoiceRepository.getById(id);

        if (invoice == null) {
            throw new Exception("Invoice not found in database");
        }

        return invoice;
    }

    public List<String> validateInvoicePositions(InvoicePositions invoicePositions) {

        List<String> errors = new ArrayList<>();
        Set<ConstraintViolation<InvoicePositions>> violations = validator.validate(invoicePositions);

        if (!violations.isEmpty()) {
            violations.stream().forEach(err -> {
                errors.add(err.getMessage());
            });
            log.info("Error during validate invoice positions");
            return errors;
        }
        log.info("Invoice positions validation passed");
        return Arrays.asList();
    }

    public InvoicePositions editInvoicePosition(InvoicePositions invoicePositions, Long id, Long gasId) throws Exception {
        InvoicePositions savedInvoicePositions = new InvoicePositions();
        //możliwość edycji: goodsAndSerives, amount
        GoodsAndServices foundGoodsAndServices = findGoodsAndServices(gasId);

        try {
            InvoicePositions foundInvoicePositions = invoicePositionRepository.getById(id);
            if (foundInvoicePositions == null) {
                throw new Exception("Invoice positions not found in database");
            }

            if (foundGoodsAndServices.getId() != foundInvoicePositions.getGoodsAndServicesId()) {
                foundInvoicePositions.setGoodsAndServicesId(foundGoodsAndServices.getId());
                log.info("GoodsAndPositions changed!");
                if (foundInvoicePositions.getAmount() != invoicePositions.getAmount()) {
                    foundInvoicePositions.setAmount(invoicePositions.getAmount());
                    log.info("Amount changed");
                }
                Double changedTotalPrice = foundInvoicePositions.getAmount() * foundGoodsAndServices.getPrice();
                foundInvoicePositions.setTotalPrice(changedTotalPrice);
                log.info("Total price updated");
            }
            if (foundInvoicePositions.getAmount() != invoicePositions.getAmount()) {
                foundInvoicePositions.setAmount(invoicePositions.getAmount());
                Double changedTotalPrice = foundInvoicePositions.getAmount() * foundGoodsAndServices.getPrice();
                foundInvoicePositions.setTotalPrice(changedTotalPrice);
                log.info("Amount changed and totalPrice updated");
            }
            validateInvoicePositions(foundInvoicePositions);
            savedInvoicePositions = invoicePositionRepository.save(foundInvoicePositions);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
        return savedInvoicePositions;
    }

    public void deleteInvoicePosition(Long id) throws Exception {
        InvoicePositions invoicePositions = invoicePositionRepository.getById(id);

        if (invoicePositions == null) {
            throw new Exception("Invoice positions not found in database");
        }
        invoicePositionRepository.deleteById(invoicePositions.getId());
    }
}
