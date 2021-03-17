package app.invoice.controllers;

import app.invoice.MockModel;
import app.invoice.models.ResetPasswordForm;
import app.invoice.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static app.invoice.utils.MapObject.asJsonString;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LoginControllerTest {

    private final String PATH = "/";

    private final MockModel mockModel = new MockModel();

    private MockMvc mockMvc;

    @Mock
    private UserService userService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(new LoginController(userService)).build();
    }

    /**
     * getResetPasswordRequestView() method
     */

    @Test
    void getResetPasswordRequestView() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(PATH + "reset/password/request"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("resetPasswordForm"))
                .andReturn();
    }

    /**
     * resetPasswordRequest() method
     * 1. resetPasswordRequest OK
     * 2. resetPasswordRequest throw Exception
     */

    @Test
    void resetPasswordRequest() throws Exception {
        ResetPasswordForm request = mockModel.generateResetPasswordForm();

        MvcResult mvcResult = mockMvc.perform(post(PATH + "reset/password/request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("success"))
                .andExpect(model().attribute("success", is("Email has been send!")))
                .andReturn();

        verify(userService, times(1)).sendEmailWithLinkAndResetPassword(any());
    }

    @Test
    void resetPasswordRequestThrowException() throws Exception {
        ResetPasswordForm request = mockModel.generateResetPasswordForm();

        doThrow(new Exception("error msg")).when(userService).sendEmailWithLinkAndResetPassword(any());

        MvcResult mvcResult = mockMvc.perform(post(PATH + "reset/password/request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(view().name("resetPasswordRequest"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", is("error msg")))
                .andReturn();

        verify(userService, times(1)).sendEmailWithLinkAndResetPassword(any());
    }

    /**
     * getResetPasswordView() method
     * 1. getResetPasswordView OK
     */

    @Test
    void getResetPasswordView() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get(PATH + "reset/password")
                .contentType(MediaType.APPLICATION_JSON)
                .param("user", "test")
                .param("pass", "password"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("resetPassword"))
                .andReturn();
    }

    /**
     * resetPassword() method
     * 1. resetPassword OK
     * 2. resetPassword (errors != null && !errors.isEmpty())
     * 3. resetPassword throw Exception
     */

    @Test
    void resetPassword() throws Exception {

        when(userService.changePassword(any(), anyString(), anyString())).thenReturn(Collections.emptyList());

        MvcResult mvcResult = mockMvc.perform(post(PATH + "reset/password")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("user", "test")
                .param("pass", "password")
                .param("newPassword", "newPassword")
                .param("confirmNewPassword", "newPassword"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("success"))
                .andExpect(model().attribute("success", is("Password successfully changed!")))
                .andReturn();

        verify(userService, times(1)).changePassword(any(), anyString(), anyString());
    }

    @Test
    public void resetPasswordErrorsIsNotNull() throws Exception {
        String errorMsg = "errorMsg";

        when(userService.changePassword(any(), anyString(), anyString())).thenReturn(Arrays.asList(errorMsg));

        MvcResult mvcResult = mockMvc.perform(post(PATH + "reset/password")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("user", "test")
                .param("pass", "password")
                .param("newPassword", "newPassword")
                .param("confirmNewPassword", "newPassword"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("resetPassword"))
                .andExpect(model().attributeExists("errors"))
                .andExpect(model().attribute("errors", hasSize(1)))
                .andExpect(model().attribute("errors", hasItem("errorMsg")))
                .andReturn();

        verify(userService, times(1)).changePassword(any(), anyString(), anyString());
    }

    @Test
    void resetPasswordThrowException() throws Exception {
        when(userService.changePassword(any(), anyString(), anyString())).thenThrow(new Exception("errorMsg"));

        MvcResult mvcResult = mockMvc.perform(post(PATH + "reset/password")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("user", "test")
                .param("pass", "password")
                .param("newPassword", "newPassword")
                .param("confirmNewPassword", "newPassword"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("resetPassword"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", is("errorMsg")))
                .andReturn();

        verify(userService, times(1)).changePassword(any(), anyString(), anyString());
    }
}
