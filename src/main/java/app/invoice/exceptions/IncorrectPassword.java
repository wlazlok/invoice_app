package app.invoice.exceptions;

public class IncorrectPassword extends RuntimeException {

    public IncorrectPassword() {
    }

    public IncorrectPassword(String msg) {
        super(msg);
    }
}
