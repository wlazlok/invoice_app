package app.invoice.models;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Data
@Table(name = "Invoice")
@Entity
@Validated
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "Invoice invoice number is blank")
    @Size(max = 10, message = "Invoice invoice number max size 10")
    String invoiceNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date dateOfIssue = null;

    @NotNull(message = "Invoice city is blank")
    @Size(max = 100, message = "Invoice city max size 100")
    String city;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date saleDate = null;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date paymentDate = null;

    @NotNull(message = "Invoice paying method is blank")
    PayingMethods payingMethod = null;

    Double totalPrice;

    //@NotNull(message = "Invoice dealer is blank")
//    Long dealerId = null;

    //@NotNull(message = "Invoice buyer is blank")
//    Long buyerId = null;

    boolean isPayed = false;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date seenDate = null;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;

    @ManyToOne
    @JoinColumn(name = "contractor_id", referencedColumnName = "id")
    Contractor contractor;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "invoice", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    List<InvoicePositions>  invoicePositions = new ArrayList<>();
}
