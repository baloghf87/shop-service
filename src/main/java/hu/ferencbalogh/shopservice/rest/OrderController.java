package hu.ferencbalogh.shopservice.rest;

import hu.ferencbalogh.shopservice.dto.CreateOrderRequest;
import hu.ferencbalogh.shopservice.dto.ListOrdersResponse;
import hu.ferencbalogh.shopservice.entity.Order;
import hu.ferencbalogh.shopservice.service.OrderCreatorService;
import hu.ferencbalogh.shopservice.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@Api(description = "Endpoints to manipulate orders")
public class OrderController {

    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderCreatorService orderCreatorService;

    @GetMapping("/list")
    @ApiOperation(value = "List orders", notes = "Accepted date/time formats:" +
            "<ul>" +
            "<li>2018-12-05T13:08:09.441+01:00</li>" +
            "<li>The format and timezone configured. Default format: 2018-12-05T13:08:09.441, default timezone: UTC</li>" +
            "<li>2018-12-05</li>" +
            "</ul>")
    public ResponseEntity<List<ListOrdersResponse>> list(
            @ApiParam(allowEmptyValue = true, value = "Lower time boundary") @RequestParam(value = "from", required = false) ZonedDateTime from,
            @ApiParam(allowEmptyValue = true, value = "Upper time boundary") @RequestParam(value = "to", required = false) ZonedDateTime to) {
        LOG.info("Listing orders from {} to {}", from, to);
        List<ListOrdersResponse> orders = orderService.list(from, to).stream()
                .map(ListOrdersResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/create")
    @ApiOperation(value = "Create order", notes = "Returns the unique identifier of the created order")
    public ResponseEntity<Integer> create(@ApiParam("The order to create") @RequestBody @Valid CreateOrderRequest request) {
        LOG.info("Creating order: {}", request);
        Order order = orderCreatorService.createOrder(request);
        orderService.add(order);
        return ResponseEntity.ok(order.getId());
    }

    @PostMapping("/{id}/recalculate")
    @ApiOperation("Recalculate prices of an existing order")
    public ResponseEntity recalculate(@ApiParam(value = "The unique identifier of the order", example = "1") @PathVariable("id") int id) {
        LOG.info("Recalculating prices of order with ID {}", id);
        orderService.recalculate(id);
        return ResponseEntity.ok().build();
    }


}
