package hu.ferencbalogh.shopservice.service;

import hu.ferencbalogh.shopservice.entity.Order;
import hu.ferencbalogh.shopservice.entity.OrderItem;
import hu.ferencbalogh.shopservice.entity.Product;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public abstract class OrderServiceTest {
    private final Product PRODUCT_1 = new Product("Test product 1", new BigDecimal("1.23"));
    private final Product PRODUCT_2 = new Product("Test product 2", new BigDecimal("2.34"));
    private final Product PRODUCT_3 = new Product("Test product 3", new BigDecimal("3.45"));

    private final List<Product> PRODUCTS = Arrays.asList(PRODUCT_1, PRODUCT_2, PRODUCT_3);

    private final ZonedDateTime CURRENT_TIME = ZonedDateTime.now();
    private final ZonedDateTime ONE_DAY_AGO = CURRENT_TIME.minus(1, ChronoUnit.DAYS);
    private final ZonedDateTime TWO_DAYS_AGO = CURRENT_TIME.minus(2, ChronoUnit.DAYS);
    private final ZonedDateTime THREE_DAYS_AGO = CURRENT_TIME.minus(3, ChronoUnit.DAYS);

    private final Order ORDER_1 = new Order("baloghf87@gmail.com", CURRENT_TIME, Arrays.asList(
            new OrderItem(PRODUCT_1, 1),
            new OrderItem(PRODUCT_2, 2),
            new OrderItem(PRODUCT_3, 3)));

    private final Order ORDER_2 = new Order("test123@gmail.com", ONE_DAY_AGO, Arrays.asList(
            new OrderItem(PRODUCT_2, 3)));

    private final Order ORDER_3 = new Order("test234@gmail.com", TWO_DAYS_AGO, Arrays.asList(
            new OrderItem(PRODUCT_1, 1),
            new OrderItem(PRODUCT_2, 3)));

    private final Order ORDER_4 = new Order("test345@gmail.com", THREE_DAYS_AGO, Arrays.asList(
            new OrderItem(PRODUCT_3, 5),
            new OrderItem(PRODUCT_1, 1)));

    private final List<Order> ORDERS = Arrays.asList(ORDER_1, ORDER_2, ORDER_3, ORDER_4);

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Test
    public void shouldAddOrder() {
        //given
        assertTrue(productService.list().isEmpty());
        assertTrue(orderService.list(null, null).isEmpty());

        //when
        PRODUCTS.forEach(productService::save);
        orderService.add(ORDER_1);

        //then
        assertNotNull(ORDER_1.getId());
        List<Order> actualOrders = orderService.list(null, null);
        assertEquals(Arrays.asList(ORDER_1), actualOrders);

        Order actualOrder = actualOrders.get(0);
        for (int i = 0; i < ORDER_1.getItems().size(); i++) {
            assertNotNull(actualOrder.getItems().get(i).getId());
        }
    }

    @Test
    public void shouldChangeProductPricesWithoutAffectingOrderPrices() {
        //given
        assertTrue(productService.list().isEmpty());
        assertTrue(orderService.list(null, null).isEmpty());

        PRODUCTS.forEach(productService::save);
        orderService.add(ORDER_1);
        assertEquals(new LinkedList<>(Arrays.asList(ORDER_1)), orderService.list(null, null));

        //when
        PRODUCTS.forEach(product -> {
            product.setPrice(product.getPrice().multiply(new BigDecimal("2")));
            productService.save(product);
        });

        //then
        assertEquals(Arrays.asList(ORDER_1), orderService.list(null, null));
    }

    @Test
    public void shouldRecalculatePrices() {
        //given
        assertTrue(productService.list().isEmpty());
        assertTrue(orderService.list(null, null).isEmpty());

        PRODUCTS.forEach(productService::save);
        orderService.add(ORDER_1);

        assertEquals(Arrays.asList(ORDER_1), orderService.list(null, null));

        //when
        BigDecimal multiplier = new BigDecimal("3");
        PRODUCTS.forEach(product -> {
            product.setPrice(product.getPrice().multiply(multiplier));
            productService.save(product);
        });

        ORDER_1.getItems().forEach(orderItem -> {
            orderItem.setUnitPrice(orderItem.getUnitPrice().multiply(multiplier));
        });

        assertNotEquals(Arrays.asList(ORDER_1), orderService.list(null, null));
        orderService.recalculate(ORDER_1);

        //then
        assertEquals(Arrays.asList(ORDER_1), orderService.list(null, null));
    }

    @Test
    public void shouldListOrdersWhenTimeRangeIsSpecified() {
        //given
        assertTrue(productService.list().isEmpty());
        assertTrue(orderService.list(null, null).isEmpty());

        //when
        PRODUCTS.forEach(productService::save);
        ORDERS.forEach(orderService::add);

        //then
        List<Order> expectedOrders = Arrays.asList(ORDER_3, ORDER_4);
        List<Order> actualOrders = orderService.list(CURRENT_TIME.minus(80, ChronoUnit.HOURS), CURRENT_TIME.minus(30, ChronoUnit.HOURS));
        assertEquals(expectedOrders, actualOrders);

        expectedOrders = Arrays.asList(ORDER_2);
        actualOrders = orderService.list(CURRENT_TIME.minus(36, ChronoUnit.HOURS), CURRENT_TIME.minus(12, ChronoUnit.HOURS));
        assertEquals(expectedOrders, actualOrders);

        expectedOrders = Arrays.asList(ORDER_1);
        actualOrders = orderService.list(CURRENT_TIME, CURRENT_TIME);
        assertEquals(expectedOrders, actualOrders);

        actualOrders = orderService.list(CURRENT_TIME.minus(10, ChronoUnit.DAYS), CURRENT_TIME.minus(5, ChronoUnit.DAYS));
        assertTrue(actualOrders.isEmpty());
    }

    @Test
    public void shouldListAllOrdersWhenNoTimeRangeIsSpecified() {
        //given
        assertTrue(productService.list().isEmpty());
        assertTrue(orderService.list(null, null).isEmpty());

        //when
        PRODUCTS.forEach(productService::save);
        ORDERS.forEach(orderService::add);

        //then
        List<Order> actualOrders = orderService.list(null, null);
        assertEquals(ORDERS, actualOrders);
    }

    @Test
    public void shouldListOrdersWhenOnlyUpperTimeLimitIsSpecified() {
        //given
        assertTrue(productService.list().isEmpty());
        assertTrue(orderService.list(null, null).isEmpty());

        //when
        PRODUCTS.forEach(productService::save);
        ORDERS.forEach(orderService::add);

        //then
        List<Order> expectedOrders = Arrays.asList(ORDER_3, ORDER_4);
        List<Order> actualOrders = orderService.list(null, CURRENT_TIME.minus(36, ChronoUnit.HOURS));
        assertEquals(expectedOrders, actualOrders);

        expectedOrders = Arrays.asList(ORDER_2, ORDER_3, ORDER_4);
        actualOrders = orderService.list(null, CURRENT_TIME.minus(12, ChronoUnit.HOURS));
        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    public void shouldListOrdersWhenOnlyLowerTimeLimitIsSpecified() {
        //given
        assertTrue(productService.list().isEmpty());
        assertTrue(orderService.list(null, null).isEmpty());

        //when
        PRODUCTS.forEach(productService::save);
        ORDERS.forEach(orderService::add);

        //then
        List<Order> expectedOrders = Arrays.asList(ORDER_1, ORDER_2, ORDER_3);
        List<Order> actualOrders = orderService.list(CURRENT_TIME.minus(50, ChronoUnit.HOURS), null);
        assertEquals(expectedOrders, actualOrders);

        expectedOrders = Arrays.asList(ORDER_1, ORDER_2);
        actualOrders = orderService.list(CURRENT_TIME.minus(30, ChronoUnit.HOURS), null);
        assertEquals(expectedOrders, actualOrders);

        expectedOrders = Arrays.asList(ORDER_1);
        actualOrders = orderService.list(CURRENT_TIME, null);
        assertEquals(expectedOrders, actualOrders);
    }
}