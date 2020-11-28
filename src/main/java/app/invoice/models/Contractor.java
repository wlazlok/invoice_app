package app.invoice.models;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@Validated
@Table(name = "contractor")
public class Contractor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "Contractor email is blank")
    @Column(name = "email")
    //regex
            String email;

    @NotNull(message = "Contractor NIP is blank")
    @Column(name = "nip")
    @Size(min = 10, max = 10, message = "Contractor nip size 10")
    String nip;

    @NotNull(message = "Contractor company name is blank")
    @Column(name = "company_name")
    @Size(max = 50, message = "Contractor company name max size 50")
    String companyName;

    @NotNull(message = "Contractor street is blank")
    @Column(name = "street")
    @Size(max = 50, message = "Contractor street max size 50")
    String street;

    @NotNull(message = "Contractor postal code is blank")
    @Column(name = "postal_code")
    @Size(max = 10, message = "Contractor postal code max size 10")
    String postalCode;

    @NotNull(message = "Contractor city is blank")
    @Column(name = "city")
    @Size(max = 100, message = "Contractor city max size 100")
    String city;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
