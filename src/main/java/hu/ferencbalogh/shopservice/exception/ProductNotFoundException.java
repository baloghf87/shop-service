package hu.ferencbalogh.shopservice.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(int id) {
        super("Product is not found with ID: " + id);
    }
}
