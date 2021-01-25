package app.invoice.controllers;

import app.invoice.models.*;
import app.invoice.repositories.GoodsAndServicesRepository;
import app.invoice.repositories.InvoicePositionRepository;
import app.invoice.repositories.InvoiceRepository;
import app.invoice.services.ContractorService;
import app.invoice.services.UserService;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.bouncycastle.math.raw.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static app.invoice.boostrap.LoadFakeInvoice.getInvoice;

@Controller
@RequestMapping("/")
public class TemplateController {

    @Autowired
    ServletContext servletContext;

    private final ContractorService contractorService;
    private final UserService userService;
    private final TemplateEngine templateEngine;
    private final GoodsAndServicesRepository goodsAndServicesRepository;
    private final InvoicePositionRepository invoicePositionRepository;
    private final InvoiceRepository invoiceRepository;


    public TemplateController(ContractorService contractorService, TemplateEngine templateEngine, UserService userService, GoodsAndServicesRepository goodsAndServicesRepository, InvoicePositionRepository invoicePositionRepository, InvoiceRepository invoiceRepository) {
        this.contractorService = contractorService;
        this.templateEngine = templateEngine;
        this.userService = userService;
        this.goodsAndServicesRepository = goodsAndServicesRepository;
        this.invoicePositionRepository = invoicePositionRepository;
        this.invoiceRepository = invoiceRepository;
    }

//    @GetMapping(value = "/contractor", produces = {MediaType.ALL_VALUE})
//    public String getContractor(){
//        return "/contractor/add-contractor";
//    }


    @GetMapping("login")
    public String getLoginPView() {
        return "login";
    }

    @GetMapping("test")
    public String getIndexView() {
        return "test";
    }

    @GetMapping("registration")
    public String getRegistrationView(Model model) {
        model.addAttribute("userForm", new User());
        model.addAttribute("users", userService.getAllUsernames());
        return "registration";
    }

    @PostMapping("registration")
    public String registerNewUser(@ModelAttribute("userForm") User userForm, BindingResult result, Model model) {
        List<String> errors = userService.validateUser(userForm);
        if (!errors.isEmpty()) {
            model.addAttribute("userForm", userForm);
            model.addAttribute("errors", errors);
            return "registration";
        }
        try {
            userService.createUser(userForm);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "registration";
        }
        model.addAttribute("success", "Account created!");
        return "login";
    }

    @GetMapping("/reset/password/request")
    public String getResetPasswordRequestView(Model model) {
        model.addAttribute("resetPasswordForm", new ResetPasswordForm());
        return "resetPasswordRequest";
    }

    @PostMapping("/reset/password/request")
    public String resetPasswordRequest(@ModelAttribute("resetPasswordForm") ResetPasswordForm resetPasswordForm, Model model) {
        try {
            userService.sendEmailWithLinkAndResetPassword(resetPasswordForm);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "resetPasswordRequest";
        }
        model.addAttribute("resetPasswordForm", new ResetPasswordForm());
        model.addAttribute("success", "Email has been send!");
        return "login";
    }

    @GetMapping("/reset/password")
    public String getResetPasswordView(Model model, @RequestParam String user, @RequestParam String pass) {
        byte[] decodedUserByte = Base64.getDecoder().decode(user.getBytes());
        byte[] decodedPassByte = Base64.getDecoder().decode(pass.getBytes());

        String decodedUser = new String(decodedUserByte, StandardCharsets.UTF_8);
        String decodedPass = new String(decodedPassByte, StandardCharsets.UTF_8);

        ChangeUserPasswordForm changeUserPasswordForm = new ChangeUserPasswordForm();
        changeUserPasswordForm.setUserName(decodedUser);
        changeUserPasswordForm.setOldPassword(decodedPass);
        model.addAttribute("changeUserPasswordForm", changeUserPasswordForm);

        return "resetPassword";
    }

    @PostMapping(path = "/reset/password",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String resetPassword(@RequestParam Map<String, String> body,
                                Model model) {
        List<String> errors = Arrays.asList();
        byte[] decodedUserByte = Base64.getDecoder().decode(body.get("user").getBytes());
        byte[] decodedPassByte = Base64.getDecoder().decode(body.get("pass").getBytes());

        String decodedUser = new String(decodedUserByte, StandardCharsets.UTF_8);
        String decodedPass = new String(decodedPassByte, StandardCharsets.UTF_8);
        System.out.println(body);
        ChangeUserPasswordForm changeUserPasswordForm = new ChangeUserPasswordForm();
        /*if (!decodedUser.equals(body.get("userName"))) {
            model.addAttribute("error", "User names are not equals!");
            changeUserPasswordForm.setUserName(decodedUser);
            model.addAttribute("changeUserPasswordForm", changeUserPasswordForm);
            return "resetPassword";
        }*/
        /*if (!decodedPass.equals(body.get("oldPassword"))) {
            model.addAttribute("error", "Passowrd from link and form are not equals!");
            changeUserPasswordForm.setUserName(decodedUser);
            model.addAttribute("changeUserPasswordForm", changeUserPasswordForm);
            return "resetPassword";
        }*/

        try {
            String pass = body.get("newPassword");
            String confirmPass = body.get("confirmNewPassword");
            changeUserPasswordForm.setUserName(decodedUser);
            changeUserPasswordForm.setOldPassword(decodedPass);
            errors = userService.changePassword(changeUserPasswordForm, pass, confirmPass);
            if (errors != null && !errors.isEmpty()) {
                model.addAttribute("errors", errors);
                return "resetPassword";
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("changeUserPasswordForm", changeUserPasswordForm);
            return "resetPassword";
        }
        model.addAttribute("success", "Password successfully changed!");
        return "login";
    }

    @GetMapping("/invoice/{id}")
    public String pdf(@PathVariable("id") String id, Model model) {
        model.addAttribute("invoice", invoiceRepository.getById(Long.valueOf(id)));
        return "invoice";
    }

//    @RequestMapping(path = "/invoice/print")
//    public ResponseEntity<?> getPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {
//
//        Invoice invoice = getInvoice();
//
//        WebContext context = new WebContext(request, response, servletContext);
//        context.setVariable("invoice", invoice);
//
//        String orderHtml = templateEngine.process("invoice", context);
//        ByteArrayOutputStream target = new ByteArrayOutputStream();
//        ConverterProperties converterProperties = new ConverterProperties();
//        converterProperties.setBaseUri("http://localhost:8080");
//        HtmlConverter.convertToPdf(orderHtml, target, converterProperties);
//
//        byte[] bytes = target.toByteArray();
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + invoice.getInvoiceNumber() + ".pdf")
//                .contentType(MediaType.APPLICATION_PDF)
//                .body(bytes);
//
//    }
//
//    @GetMapping("/invoice/create")
//    public String getCreateInvoiceView(Model model) {
//        List<PayingMethods> payingMethods = Arrays.asList(PayingMethods.values());
//        User user = userService.getUserById(1L);
//        List<GoodsAndServices> goods = new ArrayList<>();
//        goodsAndServicesRepository.findAll().forEach(goods::add);
//        model.addAttribute("goods", goods);
////        model.addAttribute("positions", new ArrayList<Integer>());
//        model.addAttribute("contractors", user.getContractors());
//        model.addAttribute("payingMethod", payingMethods);
//        model.addAttribute("invoice", new Invoice());
//        return "invoiceCreate";
//    }
//
//    @RequestMapping(value = "/invoice/create", method = RequestMethod.POST)
//    public String createInvoice(@ModelAttribute Invoice invoice, @RequestParam(value = "action") String action, Model model) {
//        String[] actionTab = action.split(";");
//        if (actionTab[0].equals("save")) {
//            if (actionTab[1].equals("null")) {
//                System.out.println("NOWA");
//                Invoice saved = invoiceRepository.save(invoice);
//                System.out.println("ZAPISANA " + saved.getGoodsAndServices().size());
//                Invoice found = invoiceRepository.getById(saved.getId());
//                System.out.println("ZNALEZIONA " + found.getGoodsAndServices().size());
//                List<GoodsAndServices> goods = new ArrayList<>();
//                goodsAndServicesRepository.findAll().forEach(goods::add);
//                User user = userService.getUserById(1L);
//                model.addAttribute("goods", goods);
//                model.addAttribute("contractors", user.getContractors());
//                model.addAttribute("invoice", saved);
//                return "invoiceCreate";
//            } else {
//                System.out.println("UPDATE");
//                Invoice foundInvoice = invoiceRepository.getById(Long.valueOf(actionTab[1]));
//                System.out.println("FOUND: " + foundInvoice.getGoodsAndServices().size());
//                System.out.println("PORTAL: " + invoice.getGoodsAndServices().size());
//                List<GoodsAndServices> goods = new ArrayList<>();
//                goodsAndServicesRepository.findAll().forEach(goods::add);
//                User user = userService.getUserById(1L);
//                model.addAttribute("invoice", foundInvoice);
//                model.addAttribute("goods", goods);
//                model.addAttribute("contractors", user.getContractors());
//                return "invoiceCreate";
//            }
//        } else if (actionTab.equals("create")) {
//
//        }
//        if (action.equals("save")) {
//            User user = userService.getUserById(1L);
//            List<GoodsAndServices> goods = new ArrayList<>();
//            goodsAndServicesRepository.findAll().forEach(goods::add);
//            //System.out.println(invoice); //path variable -> szukac po id > update -> na strone
//            Invoice savedInvoice = invoiceRepository.save(invoice);
//            invoice.getInvoicePositions().forEach(System.out::println);
//            model.addAttribute("goods", goods);
//            model.addAttribute("contractors", user.getContractors());
//            model.addAttribute("invoice", savedInvoice);
//            return "invoiceCreate";
//        } else if (action.equals("create")) {
//            System.out.println("ES");
//        }
//        return "login";
//
//        return null;
//    }
}
