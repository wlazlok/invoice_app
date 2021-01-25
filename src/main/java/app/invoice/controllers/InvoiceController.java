package app.invoice.controllers;

import app.invoice.models.Invoice;
import app.invoice.models.InvoicePositions;
import app.invoice.models.PayingMethods;
import app.invoice.models.User;
import app.invoice.repositories.ContractorRepository;
import app.invoice.repositories.GoodsAndServicesRepository;
import app.invoice.repositories.InvoicePositionRepository;
import app.invoice.repositories.InvoiceRepository;
import app.invoice.services.InvoiceService;
import app.invoice.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Controller
@RequestMapping("/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final UserService userService;
    private final InvoiceRepository invoiceRepository;
    private final InvoicePositionRepository invoicePositionRepository;
    private final ContractorRepository contractorRepository;

    public InvoiceController(InvoiceService invoiceService, UserService userService, InvoiceRepository invoiceRepository, GoodsAndServicesRepository goodsAndServicesRepository, InvoicePositionRepository invoicePositionRepository, ContractorRepository contractorRepository) {
        this.invoiceService = invoiceService;
        this.userService = userService;
        this.invoiceRepository = invoiceRepository;
        this.invoicePositionRepository = invoicePositionRepository;
        this.contractorRepository = contractorRepository;
    }

    @GetMapping("/create")
    public String getCreateInvoiceView(Model model) {
        List<PayingMethods> mayingMethods = Arrays.asList(PayingMethods.values());
        User contextUser = userService.getUserFromContext();
        model.addAttribute("goods", contextUser.getGoodsAndServices());
        model.addAttribute("invoice", new Invoice());
        model.addAttribute("payingMethods", mayingMethods);
        model.addAttribute("contractors", contextUser.getContractors());

        return "invoices/createInvoice";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createInvoice(@RequestParam(value = "action") String action, @ModelAttribute("invoice") Invoice invoice, Model model) {
        User contextUser = userService.getUserFromContext();
        List<PayingMethods> mayingMethods = Arrays.asList(PayingMethods.values());
        String[] actionTab = action.split(";");
        //podsumowanie
        if (actionTab[0].equals("sum")) {
            System.out.println("Printuje ");
            return "redirect:/invoice/" + actionTab[1];
        }
        else if (actionTab[1].equals("null")) {
            //zapis nowej
            Invoice savedInvoice = invoiceService.createInvoice(invoice, contextUser);
            model.addAttribute("goods", contextUser.getGoodsAndServices());
            model.addAttribute("invoice", savedInvoice);
            model.addAttribute("positions", savedInvoice.getInvoicePositions());
            model.addAttribute("payingMethods", mayingMethods);
            model.addAttribute("contractors", contextUser.getContractors());
            return "invoices/createInvoice";
        } else {
            //update faktury
            Invoice savedInvoice = invoiceService.updateInvoice(Long.valueOf(actionTab[1]), invoice);
            model.addAttribute("goods", contextUser.getGoodsAndServices());
            model.addAttribute("invoice", savedInvoice);
            model.addAttribute("positions", savedInvoice.getInvoicePositions().stream().distinct().collect(Collectors.toList()));
            model.addAttribute("payingMethods", mayingMethods);
            model.addAttribute("contractors", contextUser.getContractors());
            return "invoices/createInvoice";
        }
        //1. zapisz fakture
        //2. zapisz pozycje i dopisz do faktury
        //3. aktualizuj fakture
    }

//    @PostMapping("/delete")
//    public ResponseEntity<String> deleteInvoice(@RequestParam("id") String id) {
//
//        try {
//            invoiceService.deleteInvoice(Long.valueOf(id));
//        } catch (Exception ex) {
//            log.info("Error during deleting invoice: " + ex.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
//        }
//        log.info("Invoice deleted");
//        return ResponseEntity.ok("Invoice deleted");
//    }
}