package hu.ferencbalogh.shopservice.service;

import hu.ferencbalogh.shopservice.dto.CreateOrderRequest;
import hu.ferencbalogh.shopservice.entity.Order;
import hu.ferencbalogh.shopservice.entity.OrderItem;
import hu.ferencbalogh.shopservice.entity.Product;
import hu.ferencbalogh.shopservice.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderCreatorService {

    @Autowired
    private Clock clock;

    @Autowired
    private ProductService productService;

    private ZoneId zoneId;

    public Order createOrder(CreateOrderRequest request) {
        List<OrderItem> items = request.getItems().stream()
                .map(this::createOrderItem)
                .collect(Collectors.toList());

        return new Order(request.getBuyerEmail(), clock.instant().atZone(zoneId), items);
    }

    private OrderItem createOrderItem(CreateOrderRequest.CreateOrderItem item) {
        Product product = productService.getById(item.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(item.getProductId()));

        return new OrderItem(product, item.getQuantity());
    }

    @Autowired
    public void setTimeZone(@Value("${api.datetime.timezone}") String timeZone) {
        this.zoneId = ZoneId.of(timeZone);
    }
}
