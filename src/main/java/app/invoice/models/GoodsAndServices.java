package app.invoice.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "goods_and_services")
@Validated
public class GoodsAndServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Proszę podać nazwę")
    @Column(name = "name")
    @Size(max = 50, message = "Maksymalna długość pola: 50 znaków")
    String name;

    @NotNull(message = "Proszę podać cenę")
    @Column(name = "price")
    Double price;

    @NotBlank(message = "Prosze podać jednostkę")
    @Column(name = "unit")
    @Size(max = 20, message = "Maksymalna długość jednostki: 20 znaków")
    String unit;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "goodsAndServices", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    List<InvoicePositions> invoicePositions = new ArrayList<>();
}
