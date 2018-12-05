package hu.ferencbalogh.shopservice.service.impl;

import hu.ferencbalogh.shopservice.Util;
import hu.ferencbalogh.shopservice.entity.Product;
import hu.ferencbalogh.shopservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductServiceDatabaseImpl extends AbstractProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product addOrUpdate(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> getById(int id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> list() {
        return Util.toList(productRepository.findAll());
    }

}
