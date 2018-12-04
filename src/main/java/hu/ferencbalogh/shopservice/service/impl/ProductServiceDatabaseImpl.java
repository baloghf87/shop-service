package hu.ferencbalogh.shopservice.service.impl;

import hu.ferencbalogh.shopservice.Util;
import hu.ferencbalogh.shopservice.database.ProductRepository;
import hu.ferencbalogh.shopservice.entity.Product;
import hu.ferencbalogh.shopservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceDatabaseImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> list() {
        return Util.toList(productRepository.findAll());
    }

}
