package hu.ferencbalogh.shopservice.service.impl;

import hu.ferencbalogh.shopservice.entity.Order;
import hu.ferencbalogh.shopservice.entity.OrderItem;
import hu.ferencbalogh.shopservice.entity.Product;
import hu.ferencbalogh.shopservice.exception.OrderNotFoundException;
import hu.ferencbalogh.shopservice.exception.ProductNotFoundException;
import hu.ferencbalogh.shopservice.service.OrderService;
import hu.ferencbalogh.shopservice.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public abstract class AbstractOrderService implements OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractOrderService.class);

    @Autowired
    private ProductService productService;

    protected abstract Optional<Order> getById(int id);

    protected abstract Order addOrUpdate(Order order);

    public abstract List<Order> list(ZonedDateTime from, ZonedDateTime to);

    @Override
    public Order add(Order order) {
        LOG.info("Creating order: {}", order);
        addOrUpdate(order);
        LOG.info("Order is created with ID {}", order.getId());
        return order;
    }

    @Override
    public Order recalculate(int id) {
        LOG.info("Recalculating prices of order with ID {}", id);
        Order order = getById(id).orElseThrow(() -> new OrderNotFoundException(id));
        order.getItems().forEach(this::updatePrice);
        return addOrUpdate(order);
    }

    private OrderItem updatePrice(OrderItem orderItem) {
        Product product = productService.getById(orderItem.getProduct().getId())
                .orElseThrow(() -> new ProductNotFoundException(orderItem.getProduct().getId()));
        LOG.info("New price of {}: {}", orderItem, product.getPrice());
        orderItem.setUnitPrice(product.getPrice());
        return orderItem;
    }

}
