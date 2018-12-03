package hu.ferencbalogh.shopservice.service;

import hu.ferencbalogh.shopservice.entity.Order;

import java.util.Date;
import java.util.List;

public interface OrderService {
    void add(Order order);

    void recalculate(Order order);

    List<Order> list(Date from, Date to);
}
