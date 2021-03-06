package app.invoice.repositories;

import app.invoice.models.Contractor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ContractorRepository extends CrudRepository<Contractor, Long> {

    Contractor save(Contractor contractor);

    void deleteByIdGreaterThanEqual(Long id);

    Contractor getById(Long id);

    void deleteById(Long id);
}
