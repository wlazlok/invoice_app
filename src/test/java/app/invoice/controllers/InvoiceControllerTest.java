package app.invoice.controllers;

import app.invoice.MockModel;
import app.invoice.models.Invoice;
import app.invoice.models.User;
import app.invoice.repositories.InvoiceRepository;
import app.invoice.services.InvoiceService;
import app.invoice.services.UserService;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static app.invoice.utils.MapObject.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class InvoiceControllerTest {

    private final String PATH = "/invoice";

    private final MockModel mockModel = new MockModel();

    private MockMvc mockMvc;

    @Mock
    private InvoiceService invoiceService;

    @Mock
    private UserService userService;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private ITemplateEngine templateEngine;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(new InvoiceController(invoiceService, userService, invoiceRepository, templateEngine)).build();
    }

    @Test
    void getListInvoicesView() throws Exception {
        //given
        List<Invoice> invoices = Arrays.asList(mockModel.generateInvoice());
        //when
        when(userService.getAllInvoices()).thenReturn(invoices);
        //then
        MvcResult mvcResult = mockMvc.perform(get(PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("invoices/listInvoices"))
                .andExpect(model().attributeExists("invoices"))
                .andExpect(model().attribute("invoices", hasSize(1)))
                .andReturn();
    }

    @Test
    void getCreateInvoiceView() throws Exception {
        //given
        User user = mockModel.generateUserFromContext();
        //when
        when(userService.getUserFromContext()).thenReturn(user);
        //then
        MvcResult mvcResult = mockMvc.perform(get(PATH + "/create")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("invoices/createInvoice"))
                .andExpect(model().attributeExists("goods"))
                .andExpect(model().attributeExists("invoice"))
                .andExpect(model().attributeExists("payingMethods"))
                .andExpect(model().attributeExists("contractors"))
                .andReturn();
    }

    @Test
    void getInvoice() throws Exception {
        //given
        Invoice invoice = mockModel.generateInvoice();
        //when
        when(invoiceRepository.getById(anyLong())).thenReturn(invoice);
        //then
        MvcResult mvcResult = mockMvc.perform(get(PATH + "/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("invoices/showInvoice"))
                .andExpect(model().attributeExists("invoice"))
                .andExpect(model().attribute("invoice", equalTo(invoice)))
                .andReturn();
    }

    @Test
    void createNewInvoice() throws Exception {
        //given
        Invoice invoice = mockModel.generateInvoice();
        User user = mockModel.generateUserFromContext();
        //when
        when(userService.getUserFromContext()).thenReturn(user);
        when(invoiceService.validateInvoice(any(Invoice.class))).thenReturn(Collections.emptyList());
        when(invoiceService.createInvoice(any(Invoice.class), any(User.class))).thenReturn(invoice);
        //then
        MvcResult mvcResult = mockMvc.perform(post(PATH + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .param("action", "save;null")
                .content(asJsonString(invoice)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("invoices/createInvoice"))
                .andExpect(model().attribute("invoice", equalTo(invoice)))
                .andReturn();

    }

    @Test
    void createNewInvoiceValidationError() throws Exception {
        //given
        Invoice invoice = mockModel.generateInvoice();
        List<String> errors = Arrays.asList("Maksymalna długość pola numer faktury: 10 znaków");
        User user = mockModel.generateUserFromContext();
        //when
        when(userService.getUserFromContext()).thenReturn(user);
        when(invoiceService.validateInvoice(any(Invoice.class))).thenReturn(errors);
        //then
        MvcResult mvcResult = mockMvc.perform(post(PATH + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .param("action", "save;null")
                .content(asJsonString(invoice)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("invoices/createInvoice"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", hasSize(1)))
                .andReturn();
    }

    @Test
    void updateInvoice() throws Exception {
        //given
        Invoice invoice = mockModel.generateInvoice();
        User user = mockModel.generateUserFromContext();
        //when
        when(userService.getUserFromContext()).thenReturn(user);
        when(invoiceService.validateInvoice(any(Invoice.class))).thenReturn(Collections.emptyList());
        when(invoiceService.updateInvoice(anyLong(), any(Invoice.class))).thenReturn(invoice);
        //then
        MvcResult mvcResult = mockMvc.perform(post(PATH + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(invoice))
                .param("action", "save;1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("invoices/createInvoice"))
                .andExpect(model().attributeExists("invoice"))
                .andExpect(model().attribute("invoice", equalTo(invoice)))
                .andReturn();
    }

    @Test
    void updateInvoiceValidationErrors() throws Exception {
        //given
        Invoice invoice = mockModel.generateInvoice();
        User user = mockModel.generateUserFromContext();
        List<String> errors = Arrays.asList("Maksymalna długość pola numer faktury: 10 znaków");
        //when
        when(userService.getUserFromContext()).thenReturn(user);
        when(invoiceService.validateInvoice(any(Invoice.class))).thenReturn(errors);
        //then
        MvcResult mvcResult = mockMvc.perform(post(PATH + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(invoice))
                .param("action", "save;1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("invoices/createInvoice"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", hasSize(1)))
                .andReturn();
    }

    @Test
    void sumUpInvoice() throws Exception {
        //given
        Invoice invoice = mockModel.generateInvoice();
        //then
        MvcResult mvcResult = mockMvc.perform(post(PATH + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(invoice))
                .param("action", "sum;1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/invoice/1"))
                .andReturn();
    }

    @Test
    void getPDF() throws Exception {
        //given
        Invoice invoice = mockModel.generateInvoice();
        File file = new File(this.getClass().getResource("/invoice.html").toURI());
        String orderHtml = Files.toString(file, Charsets.UTF_8);
        //when
        when(invoiceService.getInvoiceById(anyLong())).thenReturn(invoice);
        when(templateEngine.process(eq("invoice"), any(WebContext.class))).thenReturn(orderHtml);
        //then
        MvcResult mvcResult = mockMvc.perform(get(PATH + "/print/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }
}
