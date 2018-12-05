package hu.ferencbalogh.shopservice.repository;

import hu.ferencbalogh.shopservice.entity.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {
    List<Order> findByOrderTimeBetween(ZonedDateTime from, ZonedDateTime to);

    List<Order> findByOrderTimeGreaterThanEqual(ZonedDateTime date);

    List<Order> findByOrderTimeLessThan(ZonedDateTime date);
}
