package hu.ferencbalogh.shopservice.rest;

import hu.ferencbalogh.shopservice.dto.CreateOrderRequest;
import hu.ferencbalogh.shopservice.dto.ListOrdersResponse;
import hu.ferencbalogh.shopservice.entity.Product;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class OrderControllerTest extends AbstractControllerTest {

    private static final int DELAY_MS = 2500;

    private Product product1;
    private Product product2;
    private List<Product> products;

    private CreateOrderRequest createOrderRequest1;
    private CreateOrderRequest createOrderRequest2;
    private CreateOrderRequest createOrderRequest3;
    private List<CreateOrderRequest> createOrderRequests;

    private ZonedDateTime orderCreationStartTime;

    private void createProductsAndOrders() {
        product1 = new Product("Test product 1", new BigDecimal("12.34"));
        product2 = new Product("Test product 2", new BigDecimal("34.56"));
        products = Arrays.asList(product1, product2);
        products.forEach(this::createProduct);

        createOrderRequest1 = new CreateOrderRequest("test123@gmail.com", Arrays.asList(
                new CreateOrderRequest.CreateOrderItem(product1.getId(), 2),
                new CreateOrderRequest.CreateOrderItem(product2.getId(), 3)));
        createOrderRequest2 = new CreateOrderRequest("test234@gmail.com", Arrays.asList(
                new CreateOrderRequest.CreateOrderItem(product2.getId(), 5)));
        createOrderRequest3 = new CreateOrderRequest("test345@gmail.com", Arrays.asList(
                new CreateOrderRequest.CreateOrderItem(product1.getId(), 1)));
        createOrderRequests = Arrays.asList(createOrderRequest1, createOrderRequest2, createOrderRequest3);

        orderCreationStartTime = ZonedDateTime.now();
        for (int i = 0; i < createOrderRequests.size(); i++) {
            Integer id = createOrder(createOrderRequests.get(i));
            Integer expectedId = i + 1;
            assertEquals(expectedId, id);
            if (i < createOrderRequests.size() - 1) {
                sleep();
            }
        }
    }

    @Test
    public void createAndListNothingWhenAllOrdersAreOutOfRange() {
        //given
        createProductsAndOrders();

        //when
        ZonedDateTime beforeFirst = orderCreationStartTime.minus(DELAY_MS, ChronoUnit.MILLIS);
        List<ListOrdersResponse> orders = getAllOrders(null, beforeFirst);

        //then
        assertTrue(orders.isEmpty());
    }

    @Test
    public void createAndListOrdersNewerThanLowerBound() {
        //given
        createProductsAndOrders();

        //when
        ZonedDateTime aftrerFirst = orderCreationStartTime.plus(DELAY_MS / 2, ChronoUnit.MILLIS);
        List<ListOrdersResponse> orders = getAllOrders(aftrerFirst, null);

        //then
        validateOrders(orders, createOrderRequest2, createOrderRequest3);
    }

    @Test
    public void createAndListOrdersOlderThanUpperBound() {
        //given
        createProductsAndOrders();

        //when
        ZonedDateTime afterSecond = orderCreationStartTime.plus(Math.round(DELAY_MS * 1.8), ChronoUnit.MILLIS);
        List<ListOrdersResponse> orders = getAllOrders(null, afterSecond);

        //then
        validateOrders(orders, createOrderRequest1, createOrderRequest2);
    }

    @Test
    public void createAndListOrdersBetweenLowerAndUpperBound() {
        //given
        createProductsAndOrders();

        //when
        ZonedDateTime afterFirst = orderCreationStartTime.plus(Math.round(DELAY_MS * 0.75), ChronoUnit.MILLIS);
        ZonedDateTime afterSecond = orderCreationStartTime.plus(Math.round(DELAY_MS * 1.5), ChronoUnit.MILLIS);
        List<ListOrdersResponse> orders = getAllOrders(afterFirst, afterSecond);

        //then
        validateOrders(orders, createOrderRequest2);
    }

    @Test
    public void recalculatePrices() {
        //given
        createProductsAndOrders();

        //when
        BigDecimal originalPrice = product1.getPrice();
        product1.setPrice(new BigDecimal("56.78"));
        updateProduct(product1);
        recalculateOrderPrices(1);

        //then
        List<ListOrdersResponse> orders = getAllOrders(null, null);
        assertEquals(createOrderRequests.size(), orders.size());
        validateItemsAndTotal(createOrderRequest1, orders.get(0));

        assertEquals(originalPrice, orders.get(2).getItems().get(0).getUnitPrice());
        assertEquals(originalPrice, orders.get(2).getTotal());
    }

    private void validateOrders(List<ListOrdersResponse> orders, CreateOrderRequest... requests) {
        assertEquals(requests.length, orders.size());
        for (int orderIdx = 0; orderIdx < requests.length; orderIdx++) {
            CreateOrderRequest request = requests[orderIdx];
            ListOrdersResponse order = orders.get(orderIdx);

            assertEquals(request.getBuyerEmail(), order.getBuyerEmail());
            long diffMs = ChronoUnit.MILLIS.between(order.getOrderTime(), orderCreationStartTime.plus(orderIdx * (DELAY_MS + DELAY_MS / 50), ChronoUnit.MILLIS));
            int maxDiffMs = DELAY_MS / 25;
            assertTrue(String.format("Difference: %d ms (max. allowed difference: %d ms)", diffMs, maxDiffMs), diffMs < maxDiffMs);
            assertEquals(request.getItems().size(), order.getItems().size());
            validateItemsAndTotal(request, order);
        }
    }

    private void validateItemsAndTotal(CreateOrderRequest request, ListOrdersResponse order) {
        BigDecimal requestTotal = BigDecimal.ZERO;
        BigDecimal responseTotal = BigDecimal.ZERO;

        assertEquals(request.getItems().size(), order.getItems().size());
        for (int itemIdx = 0; itemIdx < request.getItems().size(); itemIdx++) {
            CreateOrderRequest.CreateOrderItem requestItem = request.getItems().get(itemIdx);
            ListOrdersResponse.OrderListItem responseItem = order.getItems().get(itemIdx);

            assertEquals(requestItem.getProductId(), responseItem.getId());
            Product product = getProduct(requestItem.getProductId());
            assertNotNull(product);
            assertEquals(product.getName(), responseItem.getName());
            assertEquals(product.getPrice(), responseItem.getUnitPrice());
            assertEquals(requestItem.getQuantity(), responseItem.getQuantity());

            requestTotal = requestTotal.add(product.getPrice().multiply(new BigDecimal(requestItem.getQuantity())));
            responseTotal = responseTotal.add(responseItem.getUnitPrice().multiply(new BigDecimal(responseItem.getQuantity())));
        }

        assertEquals(requestTotal, order.getTotal());
        assertEquals(requestTotal, responseTotal);
    }

    private Product getProduct(Integer productId) {
        return products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    private void sleep() {
        try {
            Thread.sleep(DELAY_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}