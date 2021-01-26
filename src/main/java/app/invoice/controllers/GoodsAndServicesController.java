package app.invoice.controllers;

import app.invoice.models.GoodsAndServices;
import app.invoice.models.User;
import app.invoice.services.GoodsAndServicesService;
import app.invoice.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/goods")
public class GoodsAndServicesController {

    private final GoodsAndServicesService goodsAndServicesService;
    private final UserService userService;

    public GoodsAndServicesController(GoodsAndServicesService goodsAndServicesService, UserService userService) {
        this.goodsAndServicesService = goodsAndServicesService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String getCreateGoodsView(Model model) {
        model.addAttribute("good", new GoodsAndServices());
        return "goods/add-good";
    }

    @PostMapping("/create")
    public String createGood(@ModelAttribute("good") GoodsAndServices good, Model model) {
        List<String> errors = goodsAndServicesService.validateGoodsAndServices(good);
        if (!errors.isEmpty()) {
            //todo wypisanie errorw na strone
            log.info("GoodsAndServicesController.validation.errors" + errors.size());
            model.addAttribute("good", good);
            return "goods/add-good";
        }

        try {
            User user = userService.getUserFromContext();
            goodsAndServicesService.createGoodsAndServices(good, user.getId());
        } catch (Exception e) {
            log.info("GoodsAndServicesController.catch.errors " + e.getMessage());
            model.addAttribute("good", good);
            return "goods/add-good";
        }
        log.info("GoodsAndServicesController.good.crated.succesfully");
        return "redirect:/user/goods";
    }

    @GetMapping("/edit/{goodId}")
    public String getEditGoodView(@PathVariable("goodId") String id, Model model) {
        GoodsAndServices goodsAndServices;
        try {
            goodsAndServices = goodsAndServicesService.getGoodsAndServicesById(Long.valueOf(id));
        } catch (Exception e) {
            log.info("GoodsAndServicesController.good.not.found " + e.getMessage());
            return "redirect:/user/goods";
        }
        model.addAttribute("good", goodsAndServices);
        return "goods/add-good";
    }

    @PostMapping("/edit/{goodId}")
    public String editGood(@PathVariable("goodId") String id, @ModelAttribute("good") GoodsAndServices goodsAndServices, Model model) {

        try {
            goodsAndServicesService.editGoodsAndServices(goodsAndServices, Long.valueOf(id));
        } catch (Exception e) {
            log.info("GoodsAndServicesController.update.catch.error " + e.getMessage());
            model.addAttribute("good", goodsAndServices);
            //todo komunikat o bledzie
            return "goods/add-good";
        }
        log.info("GoodsAndServicesController.updated.succesfull");
        //todo komunikat o sukcecei
        return "redirect:/user/goods";
    }

    @GetMapping("/delete/{goodId}")
    public String deleteGood(@PathVariable("goodId") String id) {
        try {
            goodsAndServicesService.deleteGoodsAndServices(Long.valueOf(id));
        } catch (Exception e) {
            log.info("GoodsAndServicesController.delete.catch.error " + e.getMessage());
            //todo komunikat o bledzie
            return "redirect:/user/goods";
        }
        log.info("GoodsAndServicesController.delete.succesfull");
        //todo komunikat o sukcesie
        return "redirect:/user/goods";
    }
}
