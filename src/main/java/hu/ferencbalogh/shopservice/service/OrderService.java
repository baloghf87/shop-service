package hu.ferencbalogh.shopservice.service;

import hu.ferencbalogh.shopservice.entity.Order;

import java.time.ZonedDateTime;
import java.util.List;

public interface OrderService {
    Order add(Order order);

    Order recalculate(int id);

    List<Order> list(ZonedDateTime from, ZonedDateTime to);
}
