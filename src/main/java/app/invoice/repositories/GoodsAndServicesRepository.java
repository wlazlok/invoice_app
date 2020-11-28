package app.invoice.repositories;

import app.invoice.models.GoodsAndServices;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface GoodsAndServicesRepository extends CrudRepository<GoodsAndServices, Long> {

    void deleteByIdGreaterThanEqual(Long id);

    GoodsAndServices getById(Long id);
}
