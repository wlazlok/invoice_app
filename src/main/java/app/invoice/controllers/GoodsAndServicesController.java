package app.invoice.controllers;

import app.invoice.models.GoodsAndServices;
import app.invoice.services.GoodsAndServicesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/gas")
public class GoodsAndServicesController {

    private final GoodsAndServicesService goodsAndServicesService;

    public GoodsAndServicesController(GoodsAndServicesService goodsAndServicesService) {
        this.goodsAndServicesService = goodsAndServicesService;
    }

    @PostMapping("/create")
    public ResponseEntity<List<String>> createGoodsAndServices(@RequestBody GoodsAndServices goodsAndServices,
                                                               @RequestParam("id") String userId) {
        List<String> errors = Arrays.asList();
        try {
            errors = goodsAndServicesService.validateGoodsAndServices(goodsAndServices);

            if (!errors.isEmpty()) {
                log.info("Error during goodAndServices validation");
                throw new Exception("Goods and services not valid");
            }
            goodsAndServicesService.createGoodsAndServices(goodsAndServices, Long.valueOf(userId));
            log.info("GoodsAndServices created");
        } catch (Exception ex) {
            if (!errors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
            }
            log.info("Error during creating GoodsAndServices: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList(ex.getMessage()));
        }
        return ResponseEntity.ok(Arrays.asList("GoodsAndServices created!"));
    }

    @PostMapping("/edit")
    public ResponseEntity<List<String>> editGoodsAndServices(@RequestBody @Valid GoodsAndServices goodsAndServices,
                                                             @RequestParam("id") String id) {
        //możliwość edycji: name, price, unit
        try {
            goodsAndServicesService.editGoodsAndServices(goodsAndServices, Long.valueOf(id));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList(ex.getMessage()));
        }

        return ResponseEntity.ok(Arrays.asList("GoodsAndServices updated!"));
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteGoodsAndServices(@RequestParam("id") String id) {
        try {
            goodsAndServicesService.deleteGoodsAndServices(Long.valueOf(id));
        } catch (Exception ex) {
            log.info("Error during deleting goodsAndServices: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

        return ResponseEntity.ok("Goods And Services deleted successfully");
    }
}
