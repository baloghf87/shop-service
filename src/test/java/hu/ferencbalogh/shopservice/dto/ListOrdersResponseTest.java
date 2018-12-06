package hu.ferencbalogh.shopservice.dto;

import hu.ferencbalogh.shopservice.entity.Order;
import hu.ferencbalogh.shopservice.entity.OrderItem;
import hu.ferencbalogh.shopservice.entity.Product;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ListOrdersResponseTest {

    @Test
    public void createFromOrder() {
        //given
        Product product1 = new Product(1, "Product1", new BigDecimal("12.34"));
        Product product2 = new Product(2, "Product2", new BigDecimal("23.45"));
        Order order = new Order(123, "baloghf87@gmail.com", ZonedDateTime.now(), Arrays.asList(
                new OrderItem(product1, 2),
                new OrderItem(product2, 3)));
        BigDecimal orderTotal = order.getItems().stream()
                .map(orderItem -> orderItem.getProduct().getPrice().multiply(new BigDecimal(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //when
        ListOrdersResponse listOrdersResponse = new ListOrdersResponse(order);

        //then
        assertEquals(order.getId(), listOrdersResponse.getId());
        assertEquals(order.getOrderTime(), listOrdersResponse.getOrderTime());
        assertEquals(order.getBuyerEmail(), listOrdersResponse.getBuyerEmail());
        assertEquals(order.getItems().size(), listOrdersResponse.getItems().size());
        assertEquals(orderTotal, listOrdersResponse.getTotal());

        List<ListOrdersResponse.OrderListItem> items = listOrdersResponse.getItems();
        for (int i = 0; i < items.size(); i++) {
            ListOrdersResponse.OrderListItem responseItem = items.get(i);
            OrderItem orderItem = order.getItems().get(i);
            assertEquals(orderItem.getProduct().getId(), responseItem.getId());
            assertEquals(orderItem.getProduct().getName(), responseItem.getName());
            assertEquals(orderItem.getQuantity(), responseItem.getQuantity());
            assertEquals(orderItem.getUnitPrice(), responseItem.getUnitPrice());
        }
    }
}