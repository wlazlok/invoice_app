package app.invoice.repositories;

import app.invoice.models.InvoicePositions;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface InvoicePositionRepository extends CrudRepository<InvoicePositions, Long> {

    InvoicePositions getById(Long id);
}
