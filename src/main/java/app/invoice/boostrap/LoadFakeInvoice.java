package app.invoice.boostrap;

import app.invoice.models.*;

import java.text.DecimalFormat;
import java.util.Date;

public class LoadFakeInvoice {
    public static Invoice getInvoice() {

        User user = new User();
        user.setBankAccountNumber("88899977766633322211");
        user.setCompanyName("Test Comapny");
        user.setEmail("user@email.com");
        user.setNip("987654310");
        user.setCity("Cracow");
        user.setStreet("Akacjowa 48");
        user.setPostalCode("32-083");

        Contractor contractor = new Contractor();
        contractor.setCompanyName("Contractor Comapny");
        contractor.setEmail("constracotr@email.com");
        contractor.setNip("5554447779");
        contractor.setCity("Warsaw");
        contractor.setStreet("Towarowa 48");
        contractor.setPostalCode("51-233");

        GoodsAndServices wodaIScieki = new GoodsAndServices();
        wodaIScieki.setName("Zimna woda i ścieki");
        wodaIScieki.setPrice(10.49);
        wodaIScieki.setUnit("m3");
        wodaIScieki.setUser(user);

        GoodsAndServices podgrzewanieWody = new GoodsAndServices();
        podgrzewanieWody.setName("Podgrzewanie wody");
        podgrzewanieWody.setPrice(16.00);
        podgrzewanieWody.setUnit("m3");
        podgrzewanieWody.setUser(user);

        GoodsAndServices centralneOgrzewanie = new GoodsAndServices();
        centralneOgrzewanie.setName("Centralne ogrzewanie");
        centralneOgrzewanie.setPrice(52.00);
        centralneOgrzewanie.setUnit("GJ");
        centralneOgrzewanie.setUser(user);

        GoodsAndServices centralneOgrzewanieStale = new GoodsAndServices();
        centralneOgrzewanieStale.setName("Centralne ggrzewanie stale");
        centralneOgrzewanieStale.setPrice(0.70);
        centralneOgrzewanieStale.setUnit("m2");
        centralneOgrzewanieStale.setUser(user);

        GoodsAndServices zimnaWodaDoPodgrzania = new GoodsAndServices();
        zimnaWodaDoPodgrzania.setName("Zimna woda do podgrzania");
        zimnaWodaDoPodgrzania.setPrice(10.49);
        zimnaWodaDoPodgrzania.setUnit("m3");
        zimnaWodaDoPodgrzania.setUser(user);

        InvoicePositions invoicePositions1 = new InvoicePositions();
        invoicePositions1.setGoodsAndServices(wodaIScieki);
        invoicePositions1.setAmount(4);
        invoicePositions1.setTotalPrice(invoicePositions1.getGoodsAndServices().getPrice() * invoicePositions1.getAmount());

        InvoicePositions invoicePositions2 = new InvoicePositions();
        invoicePositions2.setGoodsAndServices(podgrzewanieWody);
        invoicePositions2.setAmount(2);
        invoicePositions2.setTotalPrice(invoicePositions2.getGoodsAndServices().getPrice() * invoicePositions2.getAmount());

        InvoicePositions invoicePositions3 = new InvoicePositions();
        invoicePositions3.setGoodsAndServices(centralneOgrzewanie);
        invoicePositions3.setAmount(2);
        invoicePositions3.setTotalPrice(invoicePositions3.getGoodsAndServices().getPrice() * invoicePositions3.getAmount());

        InvoicePositions invoicePositions4 = new InvoicePositions();
        invoicePositions4.setGoodsAndServices(centralneOgrzewanieStale);
        invoicePositions4.setAmount(72);
        invoicePositions4.setTotalPrice(invoicePositions4.getGoodsAndServices().getPrice() * invoicePositions4.getAmount());

        InvoicePositions invoicePositions5 = new InvoicePositions();
        invoicePositions5.setGoodsAndServices(zimnaWodaDoPodgrzania);
        invoicePositions5.setAmount(2);
        invoicePositions5.setTotalPrice(invoicePositions5.getGoodsAndServices().getPrice() * invoicePositions5.getAmount());

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("ABC-12/123-202");
        invoice.setDateOfIssue(new Date());
        invoice.setSaleDate(new Date());
        invoice.setPaymentDate(new Date());
        invoice.setSeenDate(new Date());
        invoice.setPayingMethod(PayingMethods.card);
        invoice.setUser(user);
        invoice.setContractor(contractor);
        invoice.getInvoicePositions().add(invoicePositions1);
        invoice.getInvoicePositions().add(invoicePositions2);
        invoice.getInvoicePositions().add(invoicePositions3);
        invoice.getInvoicePositions().add(invoicePositions4);
        invoice.getInvoicePositions().add(invoicePositions5);

        double suma = invoice.getInvoicePositions().stream().mapToDouble(InvoicePositions::getTotalPrice).sum();
        invoice.setTotalPrice(suma);

        return invoice;
    }
}
