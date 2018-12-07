package hu.ferencbalogh.shopservice.service.impl;

import hu.ferencbalogh.shopservice.entity.Product;
import hu.ferencbalogh.shopservice.exception.ProductNotFoundException;
import hu.ferencbalogh.shopservice.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public abstract class AbstractProductService implements ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractOrderService.class);

    public abstract Product addOrUpdate(Product product);

    @Override
    public Product create(Product product) {
        product.setId(null);
        LOG.info("Creating product: {}", product);
        addOrUpdate(product);
        LOG.info("Product is created with ID {}", product.getId());
        return product;
    }

    @Override
    public Product update(Product product) {
        if (product.getId() != null) {
            Optional<Product> optionalProductInDatabase = getById(product.getId());
            if (optionalProductInDatabase.isPresent()) {
                Product productInDatabase = optionalProductInDatabase.get();
                LOG.info("Updating product: {} -> {}", productInDatabase, product);
                productInDatabase.setPrice(product.getPrice());
                productInDatabase.setName(product.getName());

                return addOrUpdate(productInDatabase);
            }
        }

        throw new ProductNotFoundException(product.getId());
    }
}
