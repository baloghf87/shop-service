package hu.ferencbalogh.shopservice.rest;

import hu.ferencbalogh.shopservice.dto.CreateOrderRequest;
import hu.ferencbalogh.shopservice.entity.Order;
import hu.ferencbalogh.shopservice.service.OrderCreatorService;
import hu.ferencbalogh.shopservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderCreatorService orderCreatorService;

    @GetMapping("/list")
    public ResponseEntity<List<Order>> list(@RequestParam("from") @Nullable ZonedDateTime from,
                                            @RequestParam("to") @Nullable ZonedDateTime to) {
        return ResponseEntity.ok(orderService.list(from, to));
    }

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody @Valid CreateOrderRequest createOrderRequest) {
        return ResponseEntity.ok(orderService.add(orderCreatorService.createOrder(createOrderRequest)).getId());
    }

    @PostMapping("/{id}/recalculate")
    public ResponseEntity recalculate(@PathVariable("id") int id) {
        return ResponseEntity.ok(orderService.recalculate(id).getTotal());
    }


}
