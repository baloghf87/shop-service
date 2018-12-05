package hu.ferencbalogh.shopservice.service;

import hu.ferencbalogh.shopservice.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<Product> getById(int id);

    Product create(Product product);

    Product update(Product product);

    List<Product> list();
}
