package app.invoice.models;

public enum PayingMethods {
    cash("Cash"),
    card("Card"),
    transfer("Transfer");

    private final String method;

    PayingMethods(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
