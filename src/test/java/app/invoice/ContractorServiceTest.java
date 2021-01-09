package app.invoice;

import app.invoice.models.Contractor;
import app.invoice.models.User;
import app.invoice.repositories.ContractorRepository;
import app.invoice.repositories.UserRepository;
import app.invoice.services.ContractorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ContractorServiceTest {

    @Mock
    private ContractorRepository contractorRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ContractorService contractorService;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createContractorTest() {
        //given
        User user = new User();
        user.setId(1L);
        user.setUserName("test user");
        user.setPassword("password");
        user.setEmail("email@test.com");
        user.setNip("123123123");
        user.setStreet("Test Street");
        user.setPostalCode("12-234");
        user.setCity("Test City");
        user.setBankAccountNumber("123123123213");
        user.setConfirmPassword("password");

        Contractor contractor = new Contractor();
        contractor.setId(1L);
        contractor.setEmail("email@test.com");
        contractor.setNip("123123123");
        contractor.setCompanyName("Company name");
        contractor.setStreet("Street 12");
        contractor.setPostalCode("12-123");
        contractor.setCity("City");

        //when
        when(userRepository.getById(anyLong())).thenReturn(user);
        when(contractorRepository.save(any(Contractor.class))).thenReturn(contractor);

        //then
        Contractor contractorFound = contractorService.createContractor(contractor, anyLong());
        assertEquals(contractor, contractorFound);
    }

    @Test
    void editContractorTest() throws Exception {
        //given
        Contractor contractor = new Contractor();
        contractor.setId(1L);
        contractor.setEmail("email@test.com");
        contractor.setNip("123123123");
        contractor.setCompanyName("Company name");
        contractor.setStreet("Street 12");
        contractor.setPostalCode("12-123");
        contractor.setCity("City");

        Contractor contractorModified = new Contractor();
        contractorModified.setId(1L);
        contractorModified.setEmail("email@test.com");
        contractorModified.setNip("123123123");
        contractorModified.setCompanyName("Company name");
        contractorModified.setStreet("Street 12");
        contractorModified.setPostalCode("12-123");
        contractorModified.setCity("City Changed");

        //when
        when(contractorRepository.getById(anyLong())).thenReturn(contractor);
        when(contractorRepository.save(any(Contractor.class))).thenReturn(contractorModified);

        //then
        Contractor modifiedSavedContractor = contractorService.editContractor(contractor, anyLong());
        assertEquals(contractorModified, modifiedSavedContractor);
        assertEquals(contractorModified.getCity(), modifiedSavedContractor.getCity());
    }
}
