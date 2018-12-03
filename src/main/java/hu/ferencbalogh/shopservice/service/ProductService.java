package hu.ferencbalogh.shopservice.service;

import hu.ferencbalogh.shopservice.entity.Product;

import java.util.List;

public interface ProductService {
    void add(Product product);

    List<Product> list();

    void update(Product product);
}
