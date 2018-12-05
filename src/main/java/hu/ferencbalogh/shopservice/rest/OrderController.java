package hu.ferencbalogh.shopservice.rest;

import hu.ferencbalogh.shopservice.dto.CreateOrderRequest;
import hu.ferencbalogh.shopservice.dto.ListOrdersResponse;
import hu.ferencbalogh.shopservice.service.OrderCreatorService;
import hu.ferencbalogh.shopservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderCreatorService orderCreatorService;

    @GetMapping("/list")
    public ResponseEntity<List<ListOrdersResponse>> list(@RequestParam("from") @Nullable ZonedDateTime from,
                                                         @RequestParam("to") @Nullable ZonedDateTime to) {
        List<ListOrdersResponse> orders = orderService.list(from, to).stream()
                .map(ListOrdersResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody @Valid CreateOrderRequest createOrderRequest) {
        return ResponseEntity.ok(orderService.add(orderCreatorService.createOrder(createOrderRequest)).getId());
    }

    @PostMapping("/{id}/recalculate")
    public ResponseEntity recalculate(@PathVariable("id") int id) {
        orderService.recalculate(id);
        return ResponseEntity.ok().build();
    }


}
