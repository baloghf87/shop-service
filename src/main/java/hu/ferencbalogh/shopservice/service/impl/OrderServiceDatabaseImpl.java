package hu.ferencbalogh.shopservice.service.impl;

import hu.ferencbalogh.shopservice.Util;
import hu.ferencbalogh.shopservice.database.OrderRepository;
import hu.ferencbalogh.shopservice.database.ProductRepository;
import hu.ferencbalogh.shopservice.entity.Order;
import hu.ferencbalogh.shopservice.entity.OrderItem;
import hu.ferencbalogh.shopservice.entity.Product;
import hu.ferencbalogh.shopservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class OrderServiceDatabaseImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Order add(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order recalculate(Order order) {
        order.getItems().forEach(this::updatePrice);
        return orderRepository.save(order);
    }

    private OrderItem updatePrice(OrderItem orderItem) {
        Optional<Product> optionalProduct = productRepository.findById(orderItem.getProduct().getId());
        if (!optionalProduct.isPresent()) {
            throw new RuntimeException("Can not find product with ID: " + orderItem.getProduct().getId());
        }
        orderItem.setUnitPrice(optionalProduct.get().getPrice());
        return orderItem;
    }

    @Override
    public List<Order> list(ZonedDateTime from, ZonedDateTime to) {
        if (from == null && to == null) {
            return Util.toList(orderRepository.findAll());
        } else if (from != null && to == null) {
            return orderRepository.findByOrderTimeGreaterThanEqual(from);
        } else if (from == null && to != null) {
            return orderRepository.findByOrderTimeLessThan(to);
        } else {
            return orderRepository.findByOrderTimeBetween(from, to);
        }
    }
}
