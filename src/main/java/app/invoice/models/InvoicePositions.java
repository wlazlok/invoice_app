package app.invoice.models;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Table(name = "invoice_positions")
@Entity
@Validated
public class InvoicePositions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long invoiceId = null;

    Long goodsAndServicesId = null;

//    Double price = null; //todo czy to jest potrzebne i co to jest?

    @NotNull(message = "InvoicePositions amount is blank")
    int amount;

    Double totalPrice = null;

    @ManyToOne
    @JoinColumn(name = "goodsandservices_id", referencedColumnName = "id")
    GoodsAndServices goodsAndServices;

    @ManyToOne
    @JoinColumn(name = "contractor_id", referencedColumnName = "id")
    Invoice invoice = null;
}
