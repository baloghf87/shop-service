package hu.ferencbalogh.shopservice.service.impl;

import hu.ferencbalogh.shopservice.entity.Product;
import hu.ferencbalogh.shopservice.exception.ProductNotFoundException;
import hu.ferencbalogh.shopservice.service.ProductService;

import java.util.Optional;

public abstract class AbstractProductService implements ProductService {

    public abstract Product addOrUpdate(Product product);

    @Override
    public Product create(Product product) {
        product.setId(null);
        return addOrUpdate(product);
    }

    @Override
    public Product update(Product product) {
        if (product.getId() != null) {
            Optional<Product> optionalProductInDatabase = getById(product.getId());
            if (optionalProductInDatabase.isPresent()) {
                Product productInDatabase = optionalProductInDatabase.get();
                productInDatabase.setPrice(product.getPrice());
                productInDatabase.setName(product.getName());

                return addOrUpdate(productInDatabase);
            }
        }

        throw new ProductNotFoundException(product.getId());
    }
}
