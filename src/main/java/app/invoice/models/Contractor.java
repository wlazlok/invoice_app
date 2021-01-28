package app.invoice.models;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Data
@Entity
@Validated
@Table(name = "contractor")
public class Contractor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Pole email nie może być puste")
    @Column(name = "email")
    //regex
    String email;

    @NotBlank(message = "Pole NIP nie może być puste")
    @Column(name = "nip")
    @Size(min = 10, max = 10, message = "Rozmiar pola NIP: 10 znaków")
    String nip;

    @NotBlank(message = "Pole nazwa firmy nie może być puste")
    @Column(name = "company_name")
    @Size(max = 50, message = "Rozmiar pola nazwa firmy: 50 znaków")
    String companyName;

    @NotBlank(message = "Pole ulica nie może być puste")
    @Column(name = "street")
    @Size(max = 50, message = "Rozmiar pola ulica: 50 znaków")
    String street;

    @NotBlank(message = "Pole kod pocztowy nie może być puste")
    @Column(name = "postal_code")
    @Size(max = 10, message = "Rozmiar pola kod pocztowy: 10 znaków")
    String postalCode;

    @NotBlank(message = "Pole miasto nie może być puste")
    @Column(name = "city")
    @Size(max = 100, message = "Rozmiar pola miasto: 100 znaków")
    String city;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "contractor", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    List<Invoice> invoices = new ArrayList<>();
}
