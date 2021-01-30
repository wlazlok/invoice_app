package app.invoice.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Getter
@Setter
@Table(name = "invoice")
@Entity
//@Validated
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @NotBlank(message = "Proszę podać numer faktury")
    @Size(max = 10, message = "Maksymalna długość pola numer faktury: 10 znaków")
    String invoiceNumber;

    @NotNull(message = "Proszę podać datę wystawienia")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date dateOfIssue;

    @NotBlank(message = "Proszę podać miejsce wystawienia")
    @Size(max = 100, message = "IMaksymalan długość pola miejsce wystawienia: 100 znaków")
    String city;

    @NotNull(message = "Proszę podać datę sprzedaży")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date saleDate;

    @NotNull(message = "Proszę podać datę płatności")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date paymentDate;

    @NotNull(message = "Proszę wybrać sposób płatności")
    PayingMethods payingMethod;

    Double totalPrice;

    boolean isPayed = false;

    @Transient
//    @NotNull(message = "Proszę wybrać nabywcę")
    Long ContractorId;

    @Transient
    InvoicePositions position = new InvoicePositions();

    @Transient
    GoodsAndServices good = new GoodsAndServices();

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date seenDate = null;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;

    @ManyToOne
    @JoinColumn(name = "contractor_id", referencedColumnName = "id")
    Contractor contractor;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoice")
    List<InvoicePositions>  invoicePositions = new ArrayList<>();
}
