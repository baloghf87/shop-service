package hu.ferencbalogh.shopservice.repository;

import hu.ferencbalogh.shopservice.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {
}
