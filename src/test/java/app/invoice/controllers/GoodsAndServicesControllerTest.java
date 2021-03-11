package app.invoice.controllers;

import app.invoice.MockModel;
import app.invoice.models.GoodsAndServices;
import app.invoice.models.User;
import app.invoice.services.GoodsAndServicesService;
import app.invoice.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static app.invoice.utils.MapObject.asJsonString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class GoodsAndServicesControllerTest {

    private final String URL = "/goods";

    private final MockModel mockModel = new MockModel();

    private MockMvc mockMvc;

    @Mock
    private GoodsAndServicesService goodsAndServicesService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(new GoodsAndServicesController(goodsAndServicesService, userService)).build();
    }

    @Test
    void getCreateGoodsView() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get(URL + "/create")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("goods/add-good"))
                .andExpect(model().attributeExists("good"))
                .andReturn();
    }

    @Test
    void createGood() throws Exception {
        //given
        GoodsAndServices goodsAndServices = mockModel.generateGoodsAndServices();
        User contextUser = mockModel.generateUserFromContext();
        //when
        when(goodsAndServicesService.validateGoodsAndServices(any(GoodsAndServices.class))).thenReturn(Arrays.asList());
        when(userService.getUserFromContext()).thenReturn(contextUser);
        when(goodsAndServicesService.createGoodsAndServices(any(GoodsAndServices.class), anyLong())).thenReturn(goodsAndServices);
        //then
        MvcResult mvcResult = mockMvc.perform(post(URL + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(goodsAndServices)))
                .andExpect(status().is3xxRedirection())
                .andReturn();
    }

    @Test
    void createGoodValidationErrors() throws Exception {
        //given
        GoodsAndServices goodsAndServices = mockModel.generateGoodsAndServicesWithErrors();
        //when
        when(goodsAndServicesService.validateGoodsAndServices(any(GoodsAndServices.class))).thenReturn(Arrays.asList("Proszę podać cenę"));
        //then
        MvcResult mvcResult = mockMvc.perform(post(URL + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(goodsAndServices)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("goods/add-good"))
                .andExpect(model().attributeExists("error"))
                .andReturn();
    }

    @Test
    void createGoodThrowException() throws Exception {
        //given
        GoodsAndServices goodsAndServices = mockModel.generateGoodsAndServices();
        //when
        when(userService.getUserFromContext()).thenReturn(null);
        //then
        MvcResult mvcResult = mockMvc.perform(post(URL + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(goodsAndServices)))
                .andExpect(view().name("goods/add-good"))
                .andReturn();
    }

    @Test
    void getEditGoodView() throws Exception {
        //given
        GoodsAndServices goodsAndServices = mockModel.generateGoodsAndServices();
        String goodId = "5";
        //when
        when(goodsAndServicesService.getGoodsAndServicesById(anyLong())).thenReturn(goodsAndServices);
        //then
        MvcResult mvcResult = mockMvc.perform(get(URL + "/edit/1")
                .param("goodId", goodId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("good"))
                .andExpect(view().name("goods/add-good"))
                .andReturn();
    }

    @Test
    void getEditGoodViewThrowErrors() throws Exception {
        //given
        String goodId = "6";
        //when
        when(goodsAndServicesService.getGoodsAndServicesById(anyLong())).thenThrow(Exception.class);
        //then
        MvcResult mvcResult = mockMvc.perform(get(URL + "/edit/1")
                .param("goodId", goodId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/goods"))
                .andReturn();
    }

    @Test
    void editGoodValidationErrors() throws Exception {
        //given
        GoodsAndServices goodsAndServices = mockModel.generateGoodsAndServices();
        List<String> errors = Arrays.asList("Maksymalna długość pola: 50 znaków");
        //when
        when(goodsAndServicesService.validateGoodsAndServices(any(GoodsAndServices.class))).thenReturn(errors);
        //then
        MvcResult mvcResult = mockMvc.perform(post(URL + "/edit/{goodId}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(goodsAndServices)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("goods/add-good"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", hasSize(1)))
                .andReturn();
    }

    @Test
    void editGood() throws Exception {
        //given
        GoodsAndServices goodsAndServices = mockModel.generateGoodsAndServices();
        //when
        when(goodsAndServicesService.validateGoodsAndServices(any(GoodsAndServices.class))).thenReturn(Collections.emptyList());
        //then
        MvcResult mvcResult = mockMvc.perform(post(URL + "/edit/{goodId}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(goodsAndServices)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/goods"))
                .andReturn();
    }

    @Test
    void editGoodThrowException() throws Exception {
        //given
        GoodsAndServices goodsAndServices = mockModel.generateGoodsAndServices();
        //when
        when(goodsAndServicesService.editGoodsAndServices(any(GoodsAndServices.class), anyLong())).thenThrow(Exception.class);
        //then
        MvcResult mvcResult = mockMvc.perform(post(URL + "/edit/{goodId}", "2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(goodsAndServices)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("goods/add-good"))
                .andReturn();
    }

    @Test
    void deleteGood() throws Exception {
        //given
        String goodId = "6";
        //then
        MvcResult mvcResult = mockMvc.perform(get(URL + "/delete/1")
                .contentType(MediaType.APPLICATION_JSON)
                .param("goodId", goodId))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/goods"))
                .andReturn();
    }

    @Test
    void deleteGoodThrowErrors() throws Exception {
        //given
        String goodId = "77";
        //when
        doThrow(Exception.class).when(goodsAndServicesService).deleteGoodsAndServices(anyLong());
        //then
        MvcResult mvcResult = mockMvc.perform(get(URL + "/delete/77")
                .contentType(MediaType.APPLICATION_JSON)
                .param("goodId", goodId))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/goods"))
                .andReturn();
    }
}
