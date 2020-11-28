package app.invoice.repositories;

import app.invoice.models.Invoice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface InvoiceRepository extends CrudRepository<Invoice, Long> {

    Invoice getById(Long id);
}
