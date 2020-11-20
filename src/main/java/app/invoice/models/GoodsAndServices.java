package app.invoice.models;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "goods_and_services")
@Validated
public class GoodsAndServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "goodsandservices.name.is.null")
    @Column(name = "name")
    String name;

    @NotNull(message = "goodsandservices.price.is.null")
    @Column(name = "price")
    Double price;

    @NotNull(message = "goodsandservices.unit.is.null")
    @Column(name = "unit")
    String unit;

    @NotNull(message = "goodsandservices.user.is.null")
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
