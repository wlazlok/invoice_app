package app.invoice.models;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "Invoice")
@Entity
@Validated
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String invoiceNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date dateOfIssue = null;

    String city;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date saleDate = null;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date paymentDate = null;

    PayingMethods payingMethod = null;

    Double totalPrice;

    Long dealerId = null;

    Long buyerId = null;

    boolean isPayed = false;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date seenDate = null;
}
