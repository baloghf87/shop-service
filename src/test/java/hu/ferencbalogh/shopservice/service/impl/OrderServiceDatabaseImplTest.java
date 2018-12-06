package hu.ferencbalogh.shopservice.service.impl;

import hu.ferencbalogh.shopservice.entity.Order;
import hu.ferencbalogh.shopservice.entity.OrderItem;
import hu.ferencbalogh.shopservice.entity.Product;
import hu.ferencbalogh.shopservice.repository.OrderRepository;
import hu.ferencbalogh.shopservice.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Arrays;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {OrderServiceDatabaseImpl.class})
@Import(OrderServiceDatabaseImplTest.MockConfiguration.class)
public class OrderServiceDatabaseImplTest {

    @Autowired
    private OrderServiceDatabaseImpl orderServiceDatabaseImpl;

    @Autowired
    private OrderRepository mockOrderRepository;

    @Test
    public void getById() {
        //given
        int id = 123;

        //when
        orderServiceDatabaseImpl.getById(id);

        //then
        verify(mockOrderRepository).findById(eq(id));
    }

    @Test
    public void addOrUpdate() {
        //given
        Product product = new Product(1, "Test product", new BigDecimal("12.34"));
        Order order = new Order(1, "baloghf87@gmail.com", ZonedDateTime.now(), Arrays.asList(
                new OrderItem(product, 2)));

        //when
        orderServiceDatabaseImpl.addOrUpdate(order);

        //then
        verify(mockOrderRepository).save(eq(order));
    }

    @Test
    public void listWithoutTimeRange() {
        //given
        ZonedDateTime from = null;
        ZonedDateTime to = null;

        //when
        orderServiceDatabaseImpl.list(from, to);

        //then
        verify(mockOrderRepository).findAll();
    }

    @Test
    public void listWithLowerTimeBoundaryOnly() {
        //given
        ZonedDateTime from = ZonedDateTime.now();
        ZonedDateTime to = null;

        //when
        orderServiceDatabaseImpl.list(from, to);

        //then
        verify(mockOrderRepository).findByOrderTimeGreaterThanEqual(eq(from));
    }

    @Test
    public void listWithUpperTimeBoundaryOnly() {
        //given
        ZonedDateTime from = null;
        ZonedDateTime to = ZonedDateTime.now();

        //when
        orderServiceDatabaseImpl.list(from, to);

        //then
        verify(mockOrderRepository).findByOrderTimeLessThan(eq(to));
    }

    @Test
    public void listWithBothTimeBoundaries() {
        //given
        ZonedDateTime from = ZonedDateTime.now();
        ZonedDateTime to = ZonedDateTime.now();

        //when
        orderServiceDatabaseImpl.list(from, to);

        //then
        verify(mockOrderRepository).findByOrderTimeBetween(eq(from), eq(to));
    }

    @Configuration
    public static class MockConfiguration {
        @Bean
        public OrderRepository orderRepository() {
            return mock(OrderRepository.class);
        }

        @Bean
        public ProductService productService() {
            return mock(ProductService.class);
        }
    }
}