package hu.ferencbalogh.shopservice.service.impl;

import hu.ferencbalogh.shopservice.Util;
import hu.ferencbalogh.shopservice.entity.*;
import hu.ferencbalogh.shopservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class OrderServiceDatabaseImpl extends AbstractOrderService {

    @Autowired
    private OrderRepository orderRepository;


    @Override
    protected Optional<Order> getById(int id) {
        return orderRepository.findById(id);
    }

    @Override
    protected Order addOrUpdate(Order order) {
        return orderRepository.save(order);
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
