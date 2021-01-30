package app.invoice.controllers;

import app.invoice.models.Invoice;
import app.invoice.models.PayingMethods;
import app.invoice.models.User;
import app.invoice.repositories.InvoiceRepository;
import app.invoice.services.InvoiceService;
import app.invoice.services.UserService;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    ServletContext servletContext;

    private final InvoiceService invoiceService;
    private final UserService userService;
    private final InvoiceRepository invoiceRepository;
    private final TemplateEngine templateEngine;

    public InvoiceController(InvoiceService invoiceService, UserService userService, InvoiceRepository invoiceRepository, TemplateEngine templateEngine) {
        this.invoiceService = invoiceService;
        this.userService = userService;
        this.invoiceRepository = invoiceRepository;
        this.templateEngine = templateEngine;
    }

    @GetMapping()
    public String getListInvoicesView(Model model) {
        List<Invoice> invoices = userService.getAllInvoices();
        model.addAttribute("invoices", invoices);

        return "invoices/listInvoices";
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
        List<PayingMethods> payingMethods = Arrays.asList(PayingMethods.values());
        String[] actionTab = action.split(";");
        //podsumowanie
        if (actionTab[0].equals("sum")) {
            return "redirect:/invoice/" + actionTab[1];
        }
        else if (actionTab[1].equals("null")) {
            //zapis nowej
            List<String> errors = invoiceService.validateInvoice(invoice);
            if (!errors.isEmpty()) {
                model.addAttribute("error", errors);
                model.addAttribute("goods", contextUser.getGoodsAndServices());
                model.addAttribute("invoice", invoice);
                model.addAttribute("payingMethods", payingMethods);
                model.addAttribute("contractors", contextUser.getContractors());
                return "invoices/createInvoice";
            }
            Invoice savedInvoice = invoiceService.createInvoice(invoice, contextUser);
            model.addAttribute("goods", contextUser.getGoodsAndServices());
            model.addAttribute("invoice", savedInvoice);
            model.addAttribute("positions", savedInvoice.getInvoicePositions());
            model.addAttribute("payingMethods", payingMethods);
            model.addAttribute("contractors", contextUser.getContractors());
            return "invoices/createInvoice";
        } else {
            //update faktury
            List<String> errors = invoiceService.validateInvoice(invoice);
            if (!errors.isEmpty()) {
                model.addAttribute("error", errors);
                model.addAttribute("goods", contextUser.getGoodsAndServices());
                model.addAttribute("invoice", invoice);
                model.addAttribute("payingMethods", payingMethods);
                model.addAttribute("contractors", contextUser.getContractors());
                return "invoices/createInvoice";
            }
            Invoice savedInvoice = invoiceService.updateInvoice(Long.valueOf(actionTab[1]), invoice);
            model.addAttribute("goods", contextUser.getGoodsAndServices());
            model.addAttribute("invoice", savedInvoice);
            model.addAttribute("positions", savedInvoice.getInvoicePositions().stream().distinct().collect(Collectors.toList()));
            model.addAttribute("payingMethods", payingMethods);
            model.addAttribute("contractors", contextUser.getContractors());
            return "invoices/createInvoice";
        }
    }
    @GetMapping("/{id}")
    public String pdf(@PathVariable("id") String id, Model model) {
        model.addAttribute("invoice", invoiceRepository.getById(Long.valueOf(id)));
        return "invoices/showInvoice";
    }

    @RequestMapping(path = "/print/{id}")
    public ResponseEntity<?> getPDF(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //todo obsluga bledow itp
        Invoice invoice = invoiceService.getInvoiceById(Long.valueOf(id));

        WebContext context = new WebContext(request, response, servletContext);
        context.setVariable("invoice", invoice);

        String orderHtml = templateEngine.process("invoice", context);
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");
        HtmlConverter.convertToPdf(orderHtml, target, converterProperties);

        byte[] bytes = target.toByteArray();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + invoice.getInvoiceNumber() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
    }
}