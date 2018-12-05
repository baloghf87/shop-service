package hu.ferencbalogh.shopservice.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(int id) {
        super("Order is not found with id " + id);
    }
}
