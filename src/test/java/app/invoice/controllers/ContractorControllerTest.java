package app.invoice.controllers;

import app.invoice.MockModel;
import app.invoice.models.Contractor;
import app.invoice.models.User;
import app.invoice.services.ContractorService;
import app.invoice.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static app.invoice.utils.MapObject.asJsonString;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ContractorControllerTest {

    private final MockModel mockModel = new MockModel();

    @Mock
    ContractorService contractorService;

    @Mock
    UserService userService;

    @InjectMocks
    ContractorController controller;

    MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getCreateContractorViewTest() throws Exception {

        mockMvc.perform(get("/contractor/create")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("contractor/add-contractor"))
                .andExpect(model().attributeExists("contractor"))
                .andReturn();
    }

    @Test
    void createContractorTest() throws Exception {
        //given
        Contractor contractor = mockModel.generateContractor();
        User user = mockModel.generateUserFromContext();
        //when
        when(contractorService.validateContractor(any())).thenReturn(Collections.emptyList());
        when(userService.getUserFromContext()).thenReturn(user);
        //then
        MvcResult mvcResult = mockMvc.perform(post("/contractor/create")
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(asJsonString(contractor)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/contractors"))
                .andReturn();
    }

    @Test
    void createContractorWithErrorsTest() throws Exception {
        //given
        Contractor contractor = mockModel.generateContractorWithErrors();
        //when
        when(contractorService.validateContractor(any())).thenReturn(Arrays.asList("Pole NIP nie może być puste"));
        //then
        MvcResult mvcResult = mockMvc.perform(post("/contractor/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(contractor)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("contractor/add-contractor"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", hasSize(1)))
                .andExpect(model().attribute("error", hasItem("Pole NIP nie może być puste")))
                .andReturn();
    }

    @Test
    void getEditContractorViewTest() throws Exception {
        //given
        String contractorId = "1";
        Contractor foundContractor = mockModel.generateContractor();
        //when
        when(contractorService.getContractorById(anyLong())).thenReturn(foundContractor);
        //then

        System.out.println(contractorService.getContractorById(1L));

        MvcResult mvcResult = mockMvc.perform(get("/contractor/edit/1")
                .contentType(MediaType.APPLICATION_JSON)
                .param("contractorId", contractorId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("contractor"))
                .andExpect(model().attribute("contractor", hasProperty("city", equalTo("City"))))
                .andReturn();
    }
}
