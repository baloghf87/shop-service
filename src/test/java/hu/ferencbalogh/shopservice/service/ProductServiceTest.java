package hu.ferencbalogh.shopservice.service;

import hu.ferencbalogh.shopservice.entity.Product;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public abstract class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    public void shouldAddProduct() {
        //given
        assertTrue(productService.list().isEmpty());

        //when
        Product product = new Product("Test product", new BigDecimal("12.34"));
        productService.save(product);

        //then
        assertNotNull(product.getId());
        assertEquals(Arrays.asList(product), productService.list());
    }

    @Test
    public void shouldListProducts() {
        //given
        assertTrue(productService.list().isEmpty());
        Product product1 = new Product("Test product 1", new BigDecimal("12.34"));
        Product product2 = new Product("Test product 2", new BigDecimal("234.56"));

        //when
        List<Product> products = Arrays.asList(product1, product2);
        products.forEach(productService::save);

        //then
        List<Product> actualProducts = productService.list();
        assertEquals(products, actualProducts);
    }

    @Test
    public void shouldUpdateProduct() {
        //given
        assertTrue(productService.list().isEmpty());

        Product product1 = new Product("Test product 1", new BigDecimal("12.34"));
        Product product2 = new Product("Test product 2", new BigDecimal("234.56"));
        List<Product> products = Arrays.asList(product1, product2);
        products.forEach(productService::save);

        List<Product> actualProducts = productService.list();
        assertEquals(products, actualProducts);

        //when
        product2.setName("Test product II");
        product2.setPrice(new BigDecimal("345.67"));
        productService.save(product2);

        //then
        actualProducts = productService.list();
        assertEquals(products, actualProducts);
    }
}