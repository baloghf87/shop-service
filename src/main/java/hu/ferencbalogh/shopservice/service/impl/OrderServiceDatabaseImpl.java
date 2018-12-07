package hu.ferencbalogh.shopservice.service.impl;

import hu.ferencbalogh.shopservice.Util;
import hu.ferencbalogh.shopservice.entity.Order;
import hu.ferencbalogh.shopservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class OrderServiceDatabaseImpl extends AbstractOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceDatabaseImpl.class);

    @Autowired
    private OrderRepository orderRepository;

    @Override
    protected Optional<Order> getById(int id) {
        LOG.info("Querying order with ID {}", id);
        return orderRepository.findById(id);
    }

    @Override
    protected Order addOrUpdate(Order order) {
        LOG.info("Saving order: {}", order);
        return orderRepository.save(order);
    }

    @Override
    public List<Order> list(ZonedDateTime from, ZonedDateTime to) {
        if (from == null && to == null) {
            LOG.info("Listing all orders");
            return Util.toList(orderRepository.findAll());
        } else if (from != null && to == null) {
            LOG.info("Listing orders after {}", from);
            return orderRepository.findByOrderTimeGreaterThanEqual(from);
        } else if (from == null && to != null) {
            LOG.info("Listing orders before {}", to);
            return orderRepository.findByOrderTimeLessThan(to);
        } else {
            LOG.info("Listing orders from {} to {}", from, to);
            return orderRepository.findByOrderTimeBetween(from, to);
        }
    }
}
