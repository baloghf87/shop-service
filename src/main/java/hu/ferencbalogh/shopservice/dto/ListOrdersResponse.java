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

    public ListOrdersResponse() {
    }

    public ListOrdersResponse(Order order) {
        id = order.getId();
        buyerEmail = order.getBuyerEmail();
        orderTime = order.getOrderTime();
        items = getItems(order);
        calculateTotal();
    }

    public ListOrdersResponse(Integer id, String buyerEmail, ZonedDateTime orderTime, List<Item> items, BigDecimal total) {
        this.id = id;
        this.buyerEmail = buyerEmail;
        this.orderTime = orderTime;
        this.items = items;
        this.total = total;
    }

    private void calculateTotal() {
        total = BigDecimal.ZERO;
        for (Item item : items) {
            total = total.add(item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())));
        }
    }

    private List<Item> getItems(Order order) {
        return order.getItems().stream()
                .map(orderItem -> new Item(
                        orderItem.getProduct().getId(),
                        orderItem.getProduct().getName(),
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

    public static class Item {
        private Integer id;
        private String name;
        private BigDecimal unitPrice;
        private Integer quantity;

        public Item() {
        }

        public Item(Integer id, String name, BigDecimal unitPrice, Integer quantity) {
            this.id = id;
            this.name = name;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
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

        @Override
        public String toString() {
            return "Item{" +
                    "name='" + name + '\'' +
                    ", unitPrice=" + unitPrice +
                    ", quantity=" + quantity +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ListOrdersResponse{" +
                "id=" + id +
                ", buyerEmail='" + buyerEmail + '\'' +
                ", orderTime=" + orderTime +
                ", items=" + items +
                ", total=" + total +
                '}';
    }
}
