package hu.ferencbalogh.shopservice.service;

import hu.ferencbalogh.shopservice.entity.Product;

import java.util.List;

public interface ProductService {
    Product save(Product product);

    List<Product> list();
}
