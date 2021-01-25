package app.invoice.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
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

    @NotNull(message = "GoodsAndServices name is null")
    @Column(name = "name")
    @Size(max = 50, message = "GoodsAndServices name max size 50")
    String name;

    @NotNull(message = "GoodsAndServices price is blank")
    @Column(name = "price")
    Double price;

    @NotNull(message = "GoodsAndServices unit is blank")
    @Column(name = "unit")
    @Size(max = 20, message = "GoodsAndServices unit max size 20")
    String unit;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "goodsAndServices", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    List<InvoicePositions> invoicePositions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name= "invoice_id", referencedColumnName = "id")
    Invoice invoice;
}
