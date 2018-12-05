package hu.ferencbalogh.shopservice.service;

import hu.ferencbalogh.shopservice.dto.CreateOrderRequest;
import hu.ferencbalogh.shopservice.entity.Order;
import hu.ferencbalogh.shopservice.entity.OrderItem;
import hu.ferencbalogh.shopservice.entity.Product;
import hu.ferencbalogh.shopservice.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderCreatorService {

    @Autowired
    private ProductService productService;

    public Order createOrder(CreateOrderRequest request) {
        List<OrderItem> items = request.getItems().stream()
                .map(this::createOrderItem)
                .collect(Collectors.toList());

        return new Order(request.getBuyerEmail(), ZonedDateTime.now(), items);
    }

    private OrderItem createOrderItem(CreateOrderRequest.Item item) {
        Product product = productService.getById(item.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(item.getProductId()));

        return new OrderItem(product, item.getQuantity());
    }
}
