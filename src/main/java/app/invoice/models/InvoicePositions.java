package app.invoice.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Table(name = "invoice_positions")
@Entity
@Validated
public class InvoicePositions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    Long goodsAndServicesId = null;

//    Double price = null; //todo czy to jest potrzebne i co to jest?

    @NotNull(message = "InvoicePositions amount is blank")
    int amount;

    Double totalPrice = null;

    @ManyToOne
    @JoinColumn(name = "goodsandservices_id", referencedColumnName = "id")
    GoodsAndServices goodsAndServices;

    @ManyToOne
    @JoinColumn(name= "invoice_id", referencedColumnName = "id")
    Invoice invoice;
}
