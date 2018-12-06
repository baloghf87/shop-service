package hu.ferencbalogh.shopservice.rest;

import hu.ferencbalogh.shopservice.entity.Product;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ProductControllerTest extends AbstractControllerTest {

    @Test
    public void createAndListProducts() {
        //given
        List<Product> products = Arrays.asList(
                new Product("Test product 1", new BigDecimal("12.34")),
                new Product("Test product 2", new BigDecimal("23.45")));
        products.forEach(this::createProduct);

        //when
        List<Product> actualProducts = getAllProducts();

        //then
        assertEquals(products, actualProducts);
    }

    @Test
    public void updateProduct() {
        //given
        Product product1 = new Product("Test product 1", new BigDecimal("12.34"));
        Product product2 = new Product("Test product 2", new BigDecimal("23.45"));
        List<Product> products = Arrays.asList(product1, product2);
        products.forEach(this::createProduct);

        assertEquals(products, getAllProducts());

        //when
        product2.setPrice(new BigDecimal("34.56"));
        product2.setName("Test product II");
        assertNotEquals(products, getAllProducts());
        updateProduct(product2);

        //then
        assertEquals(products, getAllProducts());
    }

    @Test
    public void failOnUpdatingNotExistingProduct() {
        //given
        assertTrue(getAllProducts().isEmpty());
        Product product = new Product(1, "Test product 1", new BigDecimal("12.34"));

        //when
        try {
            restTemplate.postForEntity(baseUrl + "/product/update", product, String.class);
            throw new RuntimeException("It should have been failed");
        } catch (HttpClientErrorException.BadRequest e) {
            //then
            assertEquals(HttpStatus.BAD_REQUEST.value(), e.getRawStatusCode());
            assertEquals("Product is not found with ID: 1", e.getResponseBodyAsString());
        }
    }
}