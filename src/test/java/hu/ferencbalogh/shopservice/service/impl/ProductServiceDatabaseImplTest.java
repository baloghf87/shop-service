package hu.ferencbalogh.shopservice.service.impl;

import hu.ferencbalogh.shopservice.entity.Product;
import hu.ferencbalogh.shopservice.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ProductServiceDatabaseImpl.class})
@Import(ProductServiceDatabaseImplTest.MockConfiguration.class)
public class ProductServiceDatabaseImplTest {

    @Autowired
    private ProductServiceDatabaseImpl productServiceDatabaseImpl;

    @Autowired
    private ProductRepository mockProductRepository;

    @Test
    public void addOrUpdate() {
        //given
        Product product = new Product(1, "Test product", new BigDecimal("12.34"));

        //when
        productServiceDatabaseImpl.addOrUpdate(product);

        //then
        verify(mockProductRepository).save(eq(product));
    }

    @Test
    public void getById() {
        //given
        int id = 123;

        //when
        productServiceDatabaseImpl.getById(id);

        //then
        verify(mockProductRepository).findById(eq(id));
    }

    @Test
    public void list() {
        //given

        //when
        productServiceDatabaseImpl.list();

        //then
        verify(mockProductRepository).findAll();
    }

    @Configuration
    public static class MockConfiguration {
        @Bean
        public ProductRepository productRepository() {
            return mock(ProductRepository.class);
        }
    }
}