package app.invoice.controllers;

import app.invoice.models.GoodsAndServices;
import app.invoice.models.User;
import app.invoice.services.GoodsAndServicesService;
import app.invoice.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.math.raw.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

//    @PostMapping("/edit")
//    public ResponseEntity<List<String>> editGoodsAndServices(@RequestBody @Valid GoodsAndServices goodsAndServices,
//                                                             @RequestParam("id") String id) {
//        //możliwość edycji: name, price, unit
//        try {
//            goodsAndServicesService.editGoodsAndServices(goodsAndServices, Long.valueOf(id));
//        } catch (Exception ex) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList(ex.getMessage()));
//        }
//
//        return ResponseEntity.ok(Arrays.asList("GoodsAndServices updated!"));
//    }

//    @PostMapping("/create")
//    public ResponseEntity<List<String>> createGoodsAndServices(@RequestBody GoodsAndServices goodsAndServices,
//                                                               @RequestParam("id") String userId) {
//        List<String> errors = Arrays.asList();
//        try {
//            errors = goodsAndServicesService.validateGoodsAndServices(goodsAndServices);
//
//            if (!errors.isEmpty()) {
//                log.info("Error during goodAndServices validation");
//                throw new Exception("Goods and services not valid");
//            }
//            goodsAndServicesService.createGoodsAndServices(goodsAndServices, Long.valueOf(userId));
//            log.info("GoodsAndServices created");
//        } catch (Exception ex) {
//            if (!errors.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
//            }
//            log.info("Error during creating GoodsAndServices: " + ex.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList(ex.getMessage()));
//        }
//        return ResponseEntity.ok(Arrays.asList("GoodsAndServices created!"));
//    }
//
//
//    @PostMapping("/delete")
//    public ResponseEntity<String> deleteGoodsAndServices(@RequestParam("id") String id) {
//        try {
//            goodsAndServicesService.deleteGoodsAndServices(Long.valueOf(id));
//        } catch (Exception ex) {
//            log.info("Error during deleting goodsAndServices: " + ex.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
//        }
//
//        return ResponseEntity.ok("Goods And Services deleted successfully");
//    }
}
