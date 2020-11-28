package app.invoice.models;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Table(name = "User")
@Entity
@Validated
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "User username is null")
    @Column(name = "user_name")
    @Size(max = 50, message = "User user name max size 50")
    String userName;

    @Lob
    @NotNull(message = "User password is blank")
    @Column(name = "password")
    @Size(min = 5, message = "User password min size 5")
    String password;

    @NotNull(message = "User email is blank")
    @Column(name = "email")
    //regex
            String email;

    @NotNull(message = "User NIP is blank")
    @Column(name = "nip")
    @Size(min = 10, max = 10, message = "User NIP size 10")
    String nip;

    @NotNull(message = "User company name is blank")
    @Column(name = "company_name")
    @Size(max = 100, message = "User company name max size 100")
    String companyName;

    @NotNull(message = "User street is blank")
    @Column(name = "street")
    @Size(max = 50, message = "User street max size 50")
    String street;

    @NotNull(message = "User postal code is blank")
    @Column(name = "postal_code")
    @Size(max = 8, message = "User postal code ma size 8")
    String postalCode;

    @NotNull(message = "User city is blank")
    @Column(name = "city")
    @Size(max = 50, message = "User city max size 50")
    String city;

    @NotNull(message = "User bank ccount number is blank")
    @Column(name = "bank_account_number")
    @Size(max = 60, message = "User bank account number max size 60")
    String bankAccountNumber;

    @Transient
    @NotNull(message = "user.confirmpassword.is.null")
    String confirmPassword;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    List<GoodsAndServices> goodsAndServices = new ArrayList<GoodsAndServices>();

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    List<Contractor> contractors = new ArrayList<Contractor>();
}
