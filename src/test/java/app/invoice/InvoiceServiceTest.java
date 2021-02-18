package app.invoice;

import app.invoice.models.Contractor;
import app.invoice.models.Invoice;
import app.invoice.models.User;
import app.invoice.repositories.ContractorRepository;
import app.invoice.repositories.InvoicePositionRepository;
import app.invoice.repositories.InvoiceRepository;
import app.invoice.repositories.UserRepository;
import app.invoice.services.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Slf4j
public class InvoiceServiceTest {
    @Mock
    InvoiceRepository invoiceRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ContractorRepository contractorRepository;
    @Mock
    InvoicePositionRepository invoicePositionRepository;
    @InjectMocks
    InvoiceService invoiceService;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createInvoiceTest() {
        log.info("InvoiceServiceTest.createInvoiceTest");
        //given
        Invoice invoice = BuildModels.mockInvoice();
        User user = BuildModels.mockUsers().get(0);
        Contractor contractor = BuildModels.mockContractor();

        //when
        when(contractorRepository.getById(anyLong())).thenReturn(contractor);
        when(invoiceRepository.save(any())).thenReturn(invoice);
        when(invoicePositionRepository.save(any())).thenReturn(invoice.getPosition());

        //then
        Invoice savedInvoice = invoiceService.createInvoice(invoice, user);
        assertEquals(invoice, savedInvoice);
        assertNotNull(savedInvoice);
    }
}
