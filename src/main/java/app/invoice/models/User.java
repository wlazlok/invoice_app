package app.invoice.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "User")
@Validated
public class User implements UserDetails {

    private String password;
    private String username;
    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired = true;
    private boolean isEnabled = true;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotEmpty(message = "msg.err.user.email.is.empty")
    @NotNull(message = "msg.err.user.email.is.null")
    @Column(name = "email")
    //regex
    String email;

    //    @NotNull(message = "User NIP is blank")
    @Column(name = "nip")
    @Size(min = 10, max = 10, message = "msg.err.user.nip.size.{min}-{max}")
    String nip;

    //    @NotNull(message = "User company name is blank")
    @Column(name = "company_name")
    @Size(max = 100, message = "msg.err.user.companyname.max.size.{max}")
    String companyName;

    //    @NotNull(message = "User street is blank")
    @Column(name = "street")
    @Size(max = 50, message = "msg.err.user.street.max.size.{max}")
    String street;

    //    @NotNull(message = "User postal code is blank")
    @Column(name = "postal_code")
    @Size(max = 8, message = "msg.err.user.postalcode.max.size.{max}")
    String postalCode;

    //    @NotNull(message = "User city is blank")
    @Column(name = "city")
    @Size(max = 50, message = "msg.err.user.city.max.size.{max}")
    String city;

    //    @NotNull(message = "User bank ccount number is blank")
    @Column(name = "bank_account_number")
    @Size(max = 60, message = "msg.err.user.bankaccountnumber.max.size.{max}")
    String bankAccountNumber = "123123123123123";

    //    @Transient
//    @NotNull(message = "msg.err.user.confirmpassword.is.empty")
//    @NotEmpty(message = "msg.err.user.confirmpassword.is.empty")
    String confirmPassword;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    List<GoodsAndServices> goodsAndServices = new ArrayList<GoodsAndServices>();

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    List<Contractor> contractors = new ArrayList<Contractor>();

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    List<Invoice> invoices = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @NotEmpty(message = "msg.err.user.password.is.empty")
    @NotNull(message = "msg.err.user.password.is.empty")
    @Override
    public String getPassword() {
        return password;
    }

    @NotEmpty(message = "msg.err.user.username.is.empty")
    @NotNull(message = "msg.err.user.username.is.empty")
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
