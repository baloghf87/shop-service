package hu.ferencbalogh.shopservice.service.impl;

import hu.ferencbalogh.shopservice.Util;
import hu.ferencbalogh.shopservice.entity.Product;
import hu.ferencbalogh.shopservice.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductServiceDatabaseImpl extends AbstractProductService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceDatabaseImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product addOrUpdate(Product product) {
        LOG.info("Saving product: {}", product);
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> getById(int id) {
        LOG.info("Querying product with ID {}", id);
        return productRepository.findById(id);
    }

    @Override
    public List<Product> list() {
        LOG.info("Querying all products");
        return Util.toList(productRepository.findAll());
    }

}
