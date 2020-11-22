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

    @NotNull(message = "user.username.is.null")
    @Column(name = "user_name")
    String userName;

    @Lob
    @NotNull(message = "user.password.is.null")
    @Column(name = "password")
    String password;

    @NotNull(message = "user.email.is.null")
    @Column(name = "email")
    //regex
    String email;

    @NotNull(message = "user.nip.is.null")
    @Column(name = "nip")
    @Size(min = 10, max = 10, message = "user.nip.size.10")
    String nip;

    @NotNull(message = "user.companyname.is.null")
    @Column(name = "company_name")
    @Size(max = 100, message = "user.companyname.max.size.100")
    String companyName;

    @NotNull(message = "user.street.is.null")
    @Column(name = "street")
    @Size(max = 50, message = "user.street.max.size.50")
    String street;

    @NotNull(message = "user.postalcode.is.null")
    @Column(name = "postal_code")
    @Size(max = 8, message = "user.postalcoide.is.null")
    String postalCode;

    @NotNull(message = "user.city.is.null")
    @Column(name = "city")
    @Size(max = 50, message = "user.city.max.size.50")
    String city;

    @NotNull(message = "user.bankaccountnumber.is.null")
    @Column(name = "bank_account_number")
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
