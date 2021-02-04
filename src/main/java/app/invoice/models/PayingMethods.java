package app.invoice.models;

public enum PayingMethods {
    cash("Gotówka"),
    card("Karta"),
    transfer("Przelew");

    private final String method;

    PayingMethods(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
