package app.invoice.models;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Validated
@Table(name = "contractor")
public class Contractor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "contractor.email.is.null")
    @Column(name = "email")
    String email;

    @NotNull(message = "contractor.nip.is.null")
    @Column(name = "nip")
    String nip;

    @NotNull(message = "contractor.companyname.is.null")
    @Column(name = "company_name")
    String companyName;

    @NotNull(message = "contractor.street.is.null")
    @Column(name = "street")
    String street;

    @NotNull(message = "contractor.postalcode.is.null")
    @Column(name = "postal_code")
    String postalCode;

    @NotNull(message = "contractor.city.is.null")
    @Column(name = "city")
    String city;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
