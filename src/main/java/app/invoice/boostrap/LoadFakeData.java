package app.invoice.boostrap;

import app.invoice.models.Contractor;
import app.invoice.models.GoodsAndServices;
import app.invoice.models.User;
import app.invoice.repositories.ContractorRepository;
import app.invoice.repositories.GoodsAndServicesRepository;
import app.invoice.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

@Slf4j
@Component
public class LoadFakeData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final GoodsAndServicesRepository goodsAndServicesRepository;
    private final ContractorRepository contractorRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public LoadFakeData(UserRepository userRepository, GoodsAndServicesRepository goodsAndServicesRepository, ContractorRepository contractorRepository) {
        this.userRepository = userRepository;
        this.goodsAndServicesRepository = goodsAndServicesRepository;
        this.contractorRepository = contractorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        User fakeUser = new User();
//
//        fakeUser.setPassword(bCryptPasswordEncoder.encode("admin"));
//        fakeUser.setUsername("admin");
//        fakeUser.setEmail("test@email.com");
//        fakeUser.setNip("6462933516");
//        fakeUser.setCompanyName("Test Company sp.z.o.o");
//        fakeUser.setStreet("Example 25");
//        fakeUser.setPostalCode("22-933");
//        fakeUser.setCity("Cracow");
//        fakeUser.setBankAccountNumber("61 1090 1014 0000 0712 1981 2874");
//        fakeUser.setConfirmPassword("admin");
//        User createdUser = userRepository.save(fakeUser);
//
//        GoodsAndServices goodsAndServices = new GoodsAndServices();
//        goodsAndServices.setName("Test good");
//        goodsAndServices.setPrice(12.64);
//        goodsAndServices.setUnit("Sztuka");
//        goodsAndServices.setUser(createdUser);
//
//        Contractor contractor = new Contractor();
//        contractor.setCity("Balice");
//        contractor.setCompanyName("Test company");
//        contractor.setEmail("test@email.com");
//        contractor.setNip("6462933516");
//        contractor.setPostalCode("33-444");
//        contractor.setStreet("Sample Street 44");
//        contractor.setIdUser(78L);
//
//        GoodsAndServices goodResult = goodsAndServicesRepository.save(goodsAndServices);
//        Contractor createdContractor = contractorRepository.save(contractor);
//
//        createdUser.setGoodsAndServices(Arrays.asList(goodResult));
//        createdUser.setContractors(Arrays.asList(createdContractor));
//        createdUser = userRepository.save(createdUser);
//
//        System.out.println(createdUser.getContractors().size() + "  " + createdUser.getGoodsAndServices().size());

//        contractorRepository.deleteByIdGreaterThanEqual(2L);
//        userRepository.deleteUserByIdGreaterThanEqual(2L);
//        goodsAndServicesRepository.deleteByIdGreaterThanEqual(2L);
//        log.info("New user with username " + createdUser.getUserName() + " created!");
    }
}
