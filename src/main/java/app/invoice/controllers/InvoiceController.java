package app.invoice.controllers;

import app.invoice.models.Invoice;
import app.invoice.services.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createInvoice(@RequestBody Invoice invoice,
                                                @RequestParam("userId") String userId,
                                                @RequestParam("id") String buyerId) {
        try {
            invoiceService.validateInvoice(invoice);
            invoiceService.createInvoice(invoice, Long.valueOf(buyerId), Long.valueOf(userId));

        } catch (Exception ex) {
            log.info("Error during creating invoice:  " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

        return ResponseEntity.ok("Invoice created");
    }

    @PostMapping("/edit")
    public ResponseEntity<String> editInvoice(@RequestBody Invoice invoice,
                                              @RequestParam("id") String id) {
        //możlwość edycji: invoiceNumber, city, payingMethod
        //możlwość edytowania dat do ustalenia
        try {
            invoiceService.editInvoice(invoice, Long.valueOf(id));
        } catch (Exception ex) {
            log.info("Error during updating invoice: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

        return ResponseEntity.ok("Invoice updated!");
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteInvoice(@RequestParam("id") String id) {

        try {
            invoiceService.deleteInvoice(Long.valueOf(id));
        } catch (Exception ex) {
            log.info("Error during deleting invoice: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        log.info("Invoice deleted");
        return ResponseEntity.ok("Invoice deleted");
    }
}
