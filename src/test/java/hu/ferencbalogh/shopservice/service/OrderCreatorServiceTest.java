package hu.ferencbalogh.shopservice.service;


import hu.ferencbalogh.shopservice.dto.CreateOrderRequest;
import hu.ferencbalogh.shopservice.entity.Order;
import hu.ferencbalogh.shopservice.entity.OrderItem;
import hu.ferencbalogh.shopservice.entity.Product;
import hu.ferencbalogh.shopservice.exception.ProductNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {OrderCreatorService.class, OrderCreatorServiceTest.MockProductServiceConfiguration.class})
public class OrderCreatorServiceTest {

    private static final List<Product> PRODUCTS = Arrays.asList(
            new Product(1, "Test product 1", new BigDecimal("12.34")),
            new Product(2, "Test product 2", new BigDecimal("23.45")));

    @Autowired
    private OrderCreatorService orderCreatorService;

    @Test
    public void shouldCreateOrder() {
        List<CreateOrderRequest.Item> items = Arrays.asList(
                new CreateOrderRequest.Item(1, 1),
                new CreateOrderRequest.Item(2, 2)
        );
        CreateOrderRequest request = new CreateOrderRequest("test123@gmail.com", items);

        Order order = orderCreatorService.createOrder(request);

        assertNull(order.getId());
        assertEquals(request.getBuyerEmail(), order.getBuyerEmail());
        assertTrue(ChronoUnit.MILLIS.between(ZonedDateTime.now(), order.getOrderTime()) < 100);
        List<OrderItem> expectedOrderItems = items.stream()
                .map(item -> new OrderItem(getProduct(item), item.getQuantity()))
                .collect(Collectors.toList());
        assertEquals(expectedOrderItems, order.getItems());
    }

    @Test(expected = ProductNotFoundException.class)
    public void shouldFailWhenProductDoesNotExist() {
        //given
        CreateOrderRequest request = new CreateOrderRequest("test123@gmail.com", Arrays.asList(
                new CreateOrderRequest.Item(3, 1)
        ));

        //when
        orderCreatorService.createOrder(request);

        //then it should fail
    }

    private Product getProduct(CreateOrderRequest.Item item) {
        return PRODUCTS.stream()
                .filter(product -> product.getId().equals(item.getProductId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException());
    }

    public static class MockProductServiceConfiguration {
        @Bean
        public ProductService productService() {
            ProductService mockProductService = mock(ProductService.class);
            PRODUCTS.forEach(product -> when(mockProductService.getById(eq(product.getId()))).thenReturn(Optional.of(product)));
            return mockProductService;
        }
    }

}