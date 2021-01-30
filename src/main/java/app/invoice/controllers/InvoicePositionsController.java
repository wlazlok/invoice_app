package app.invoice.controllers;

import app.invoice.models.InvoicePositions;
import app.invoice.services.InvoicePositionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Deprecated
@Slf4j
@RestController
@RequestMapping("/invoice/positions")
public class InvoicePositionsController {

    private final InvoicePositionService invoicePositionService;

    public InvoicePositionsController(InvoicePositionService invoicePositionService) {
        this.invoicePositionService = invoicePositionService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createInvoicePositions(@RequestBody InvoicePositions invoicePositions,
                                                         @RequestParam("invoiceId") String invoiceId,
                                                         @RequestParam("gasId") String gasId) {
        try {
            invoicePositionService.createInvoicePosition(invoicePositions, Long.valueOf(gasId), Long.valueOf(invoiceId));
        } catch (Exception ex) {
            log.info("Error during creatin invoice positions: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

        log.info("Invoice positions created");
        return ResponseEntity.ok("Invoice positions created");
    }

    @PostMapping("/edit")
    public ResponseEntity<String> editInvoicePosition(@RequestBody InvoicePositions invoicePositions,
                                                      @RequestParam("positionsId") String positionsId,
                                                      @RequestParam("gasId") String gasId) {
        //możliwość edycji: goodsAndSerives, amount
        try {
            invoicePositionService.editInvoicePosition(invoicePositions, Long.valueOf(positionsId), Long.valueOf(gasId));
        } catch (Exception ex) {
            log.info("Error during updating invoice positions: "+ ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        log.info("Invoice positions updated successfully");
        return ResponseEntity.ok("Invoice positions updated!");
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteInvoicePositions(@RequestParam("id") String id) {

        try {
            invoicePositionService.deleteInvoicePosition(Long.valueOf(id));
        } catch (Exception ex) {
            log.info("Error during deleting invoice positions: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        log.info("Invoice positions deleted successfully");
        return ResponseEntity.ok("Invoice positions deleted successfully");
    }
}
