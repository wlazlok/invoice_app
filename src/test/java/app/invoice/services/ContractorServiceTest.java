package app.invoice.services;

import app.invoice.MockModel;
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
import org.springframework.security.core.parameters.P;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ContractorServiceTest {

    private final MockModel mockModel = new MockModel();

    @Mock
    private ContractorRepository contractorRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ContractorService contractorService;

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    @Mock
    Validator validator = factory.getValidator();

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createContractorTest() {
        //given
        User user = new User();
        user.setId(1L);
        user.setUsername("test user");
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

    @Test
    void getContractorByIdPassTest() throws Exception {
        //given
        Contractor contractor = new Contractor();
        contractor.setId(1L);
        contractor.setEmail("email@test.com");
        contractor.setNip("123123123");
        contractor.setCompanyName("Company name");
        contractor.setStreet("Street 12");
        contractor.setPostalCode("12-123");
        contractor.setCity("City");

        //when
        when(contractorRepository.getById(anyLong())).thenReturn(contractor);

        //then
        Contractor contractorFound = contractorService.getContractorById(1L);
        assertEquals(contractor, contractorFound);
    }

    @Test
    void getContractorByIdFailTest() throws Exception {
        //given
        Contractor contractor = new Contractor();
        contractor.setId(1L);
        contractor.setEmail("email@test.com");
        contractor.setNip("123123123");
        contractor.setCompanyName("Company name");
        contractor.setStreet("Street 12");
        contractor.setPostalCode("12-123");
        contractor.setCity("City");

        //when
        when(contractorRepository.getById(anyLong())).thenReturn(null);

        //then
        try {
            Contractor contractorFound = contractorService.getContractorById(1L);
        } catch (Exception ex) {
            assertTrue(ex instanceof Exception);
            assertEquals("Contractor not found in database!", ex.getMessage());
        }
    }

    /**
     * validateContractor() method
     * 1. validateContractor OK
     * 2. validateContractor (!violations.isEmpty())
     */

    @Test
    void validateContractorHasErrors() {
        Contractor contractor = mockModel.generateContractor();
        contractor.setCity(null);

        List<String> result = contractorService.validateContractor(contractor);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void validateContractor() {
        Contractor contractor = mockModel.generateContractor();
        contractor.setNip("1234567890");

        List<String> result = contractorService.validateContractor(contractor);

        assertNotNull(result);
        System.out.println(result);
        assertTrue(result.isEmpty());
    }

    /**
     * deleteContractor() method
     * 1. deleteContractor OK
     * 2. deleteContractor throw Exception
     */

    @Test
    void deleteContractor() throws Exception {
        Contractor contractor = mockModel.generateContractor();

        when(contractorRepository.getById(anyLong())).thenReturn(contractor);

        contractorService.deleteContractor(anyLong());
    }

    @Test
    void deleteContractorThrowException() {
        when(contractorRepository.getById(anyLong())).thenReturn(null);

        try {
            contractorService.deleteContractor(anyLong());
        } catch (Exception ex) {
            assertNotNull(ex);
            assertEquals("Contractor not found in database!", ex.getMessage());
        }
    }
}
