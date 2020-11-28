package app.invoice.services;

import app.invoice.models.Contractor;
import app.invoice.models.GoodsAndServices;
import app.invoice.models.User;
import app.invoice.repositories.GoodsAndServicesRepository;
import app.invoice.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

@Slf4j
@Service
public class GoodsAndServicesService {

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    private final UserRepository userRepository;
    private final GoodsAndServicesRepository goodsAndServicesRepository;

    public GoodsAndServicesService(UserRepository userRepository, GoodsAndServicesRepository goodsAndServicesRepository) {
        this.userRepository = userRepository;
        this.goodsAndServicesRepository = goodsAndServicesRepository;
    }

    public GoodsAndServices createGoodsAndServices(GoodsAndServices goodsAndServices, Long userId) {

        User foundUser = userRepository.getById(userId);
        goodsAndServices.setUser(foundUser);

        GoodsAndServices goodsAndServicesSaved = goodsAndServicesRepository.save(goodsAndServices);
        foundUser.getGoodsAndServices().add(goodsAndServicesSaved);

        userRepository.save(foundUser);

        return goodsAndServicesSaved;
    }

    public List<String> validateGoodsAndServices(GoodsAndServices goodsAndServices) {
        List<String> errors = new ArrayList<>();
        Set<ConstraintViolation<GoodsAndServices>> violations = validator.validate(goodsAndServices);

        if (!violations.isEmpty()) {
            violations.stream().forEach(err -> {
                errors.add(err.getMessage());
            });
            log.info("Error during validate goodsAndServices");
            return errors;
        }
        log.info("GoodsAndServices validation passed");
        return Arrays.asList();
    }

    public GoodsAndServices editGoodsAndServices(GoodsAndServices goodsAndServices, Long id) throws Exception {
        List<String> errors = Arrays.asList();
        GoodsAndServices savedGoodsAndServices = new GoodsAndServices();
        //możliwość edycji: name, price, unit
        try {
            validateGoodsAndServices(goodsAndServices);

            GoodsAndServices foundGoodsAndServices = getGoodsAndServicesById(id);

            if (!foundGoodsAndServices.getName().equals(goodsAndServices.getName())) {
                foundGoodsAndServices.setName(goodsAndServices.getName());
                log.info("Name changed");
            }
            if (foundGoodsAndServices.getPrice() != goodsAndServices.getPrice()) {
                foundGoodsAndServices.setPrice(goodsAndServices.getPrice());
                log.info("Price changed");
            }
            if (!foundGoodsAndServices.getUnit().equals(goodsAndServices.getUnit())) {
                foundGoodsAndServices.setUnit(goodsAndServices.getUnit());
                log.info("Unit changed");
            }

            savedGoodsAndServices = goodsAndServicesRepository.save(foundGoodsAndServices);
            log.info("GoodsAndServices saved in DB");

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }

        return savedGoodsAndServices;
    }

    public GoodsAndServices getGoodsAndServicesById(Long id) throws Exception {
        GoodsAndServices goodsAndServices = goodsAndServicesRepository.getById(id);

        if (goodsAndServices == null) {
            throw new Exception("GoodsAndServices not found in database");
        }
        return goodsAndServices;
    }

    public void deleteGoodsAndServices(Long id) throws Exception {

        GoodsAndServices goodsAndServices = goodsAndServicesRepository.getById(id);

        if (goodsAndServices == null) {
            throw new Exception("Goods and services not found in database");
        }
        log.info("GoodsAndServices deleted");
        goodsAndServicesRepository.deleteById(goodsAndServices.getId());
    }
}
