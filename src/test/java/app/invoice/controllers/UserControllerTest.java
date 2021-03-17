package app.invoice.controllers;

import app.invoice.MockModel;
import app.invoice.exceptions.IncorrectPassword;
import app.invoice.models.User;
import app.invoice.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static app.invoice.utils.MapObject.asJsonString;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    private final String PATH = "/user";

    private final MockModel mockModel = new MockModel();

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    /**
     * getContractorsView() method
     */

    @Test
    void getContractorsView() throws Exception {
        User user = mockModel.generateUserFromContext();

        when(userService.getUserFromContext()).thenReturn(user);

        MvcResult mvcResult = mockMvc.perform(get(PATH + "/contractors")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("contractor/contractors"))
                .andExpect(model().attributeExists("contractors"))
                .andReturn();

        verify(userService, times(1)).getUserFromContext();
    }

    /**
     * getGoodsAndServices() method
     */

    @Test
    void getGoodsAndServices() throws Exception {
        User user = mockModel.generateUserFromContext();

        when(userService.getUserFromContext()).thenReturn(user);

        MvcResult mvcResult = mockMvc.perform(get(PATH + "/goods")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("goods/goods-services"))
                .andExpect(model().attributeExists("goods"))
                .andReturn();

        verify(userService, times(1)).getUserFromContext();
    }

    /**
     * getEditUserView() method
     * 1. getEditUserView OK
     * 2. getEditUserView throw Exception
     */

    @Test
    void getEditUserView() throws Exception {
        User user = mockModel.generateUserFromContext();

        when(userService.getUserFromContext()).thenReturn(user);

        MvcResult mvcResult = mockMvc.perform(get(PATH + "/edit")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/edit-user"))
                .andExpect(model().attributeExists("user"))
                .andReturn();

        verify(userService, times(1)).getUserFromContext();
    }

    @Test
    void getEditUserViewThrowException() throws Exception {

        given(userService.getUserFromContext()).willAnswer(invocationOnMock -> {
            throw new Exception("error");
        });

        MvcResult mvcResult = mockMvc.perform(get(PATH + "/edit")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        verify(userService, times(1)).getUserFromContext();
    }

    /**
     * editUser() method
     * 1. editUser OK
     * 2. editUser (!errors.isEmpty())
     * 3. editUser throw IncorrectPassword
     * 4. editUser throw Exception
     */

    @Test
    void editUser() throws Exception {
        User request = mockModel.generateUserFromContext();
        User dbUser = mockModel.generateUserFromContext();
        User savedUser = mockModel.generateUserFromContext();
        savedUser.setCity("New City");

        when(userService.getUserFromContext()).thenReturn(dbUser);
        when(userService.validateUser(any())).thenReturn(Collections.emptyList());
        when(userService.editUser(any(), any())).thenReturn(savedUser);

        MvcResult mvcResult = mockMvc.perform(post(PATH + "/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/edit-user"))
                .andExpect(model().attribute("error", is("Dane zostały zaktualizowane")))
                .andExpect(model().attributeExists("user"))
                .andReturn();

        verify(userService, times(1)).getUserFromContext();
        verify(userService, times(1)).validateUser(any());
        verify(userService, times(1)).editUser(any(), any());
    }

    @Test
    void editUserHasErrors() throws Exception {
        User request = mockModel.generateUserFromContext();
        User dbUser = mockModel.generateUserFromContext();
        String errorMsg = "errorMsg";

        when(userService.getUserFromContext()).thenReturn(dbUser);
        when(userService.validateUser(any())).thenReturn(Arrays.asList(errorMsg));

        MvcResult mvcResult = mockMvc.perform(post(PATH + "/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/edit-user"))
                .andExpect(model().attribute("error", hasItem("errorMsg")))
                .andExpect(model().attributeExists("user"))
                .andReturn();

        verify(userService, times(1)).getUserFromContext();
        verify(userService, times(1)).validateUser(any());
        verify(userService, times(0)).editUser(any(), any());
    }

    @Test
    void editUserThrowIncorrectPassword() throws Exception {
        User request = mockModel.generateUserFromContext();
        User dbUser = mockModel.generateUserFromContext();

        when(userService.getUserFromContext()).thenReturn(dbUser);
        when(userService.validateUser(any())).thenReturn(Collections.emptyList());
        when(userService.editUser(any(), any())).thenThrow(new IncorrectPassword("Wrong Password!"));

        MvcResult mvcResult = mockMvc.perform(post(PATH + "/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/edit-user"))
                .andExpect(model().attribute("error", is("Wrong Password!")))
                .andExpect(model().attributeExists("user"))
                .andReturn();

        verify(userService, times(1)).getUserFromContext();
        verify(userService, times(1)).validateUser(any());
        verify(userService, times(1)).editUser(any(), any());
    }

    @Test
    void editUserThrowException() throws Exception {
        User request = mockModel.generateUserFromContext();
        User dbUser = mockModel.generateUserFromContext();

        when(userService.getUserFromContext()).thenReturn(dbUser);
        when(userService.validateUser(any())).thenReturn(Collections.emptyList());

        given(userService.editUser(any(), any())).willAnswer(invocationOnMock -> {
            throw new Exception("Exception");
        });

        MvcResult mvcResult = mockMvc.perform(post(PATH + "/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/edit-user"))
                .andExpect(model().attribute("error", is("Exception")))
                .andExpect(model().attributeExists("user"))
                .andReturn();

        verify(userService, times(1)).getUserFromContext();
        verify(userService, times(1)).validateUser(any());
        verify(userService, times(1)).editUser(any(), any());
    }
}
