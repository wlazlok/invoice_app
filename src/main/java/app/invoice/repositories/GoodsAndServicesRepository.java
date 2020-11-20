package app.invoice.repositories;

import app.invoice.models.GoodsAndServices;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface GoodsAndServicesRepository extends CrudRepository<GoodsAndServices, Long> {

    GoodsAndServices save(GoodsAndServices goodsAndServices);

    @Transactional
    @Modifying
    void deleteByIdGreaterThanEqual(Long id);

}
