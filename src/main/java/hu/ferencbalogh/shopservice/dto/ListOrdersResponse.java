package hu.ferencbalogh.shopservice.dto;

import hu.ferencbalogh.shopservice.entity.Order;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ListOrdersResponse {
    private Integer id;
    private String buyerEmail;
    private ZonedDateTime orderTime;
    private List<Item> items;
    private BigDecimal total;

    public ListOrdersResponse(Order order) {
        id = order.getId();
        buyerEmail = order.getBuyerEmail();
        orderTime = order.getOrderTime();
        items = getItems(order);
        calculateTotal();
    }

    private void calculateTotal() {
        total = BigDecimal.ZERO;
        for (Item item : items) {
            total = total.add(item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())));
        }
    }

    private List<Item> getItems(Order order) {
        return order.getItems().stream()
                .map(orderItem -> new Item(orderItem.getProduct().getName(),
                        orderItem.getUnitPrice(),
                        orderItem.getQuantity()))
                .collect(Collectors.toList());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public ZonedDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(ZonedDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    private static class Item {
        private String name;
        private BigDecimal unitPrice;
        private Integer quantity;

        public Item(String name, BigDecimal unitPrice, Integer quantity) {
            this.name = name;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}
