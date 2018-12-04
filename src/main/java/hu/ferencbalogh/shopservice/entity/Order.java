package hu.ferencbalogh.shopservice.entity;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

public class Order {
    private Integer id;
    private String buyerEmail;
    private ZonedDateTime orderTime;
    private List<OrderItem> items;

    public Order() {
    }

    public Order(String buyerEmail, ZonedDateTime orderTime, List<OrderItem> items) {
        this.buyerEmail = buyerEmail;
        this.orderTime = orderTime;
        this.items = items;
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

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                Objects.equals(buyerEmail, order.buyerEmail) &&
                Objects.equals(orderTime, order.orderTime) &&
                Objects.equals(items, order.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, buyerEmail, orderTime, items);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", buyerEmail='" + buyerEmail + '\'' +
                ", orderTime=" + orderTime +
                ", items=" + items +
                '}';
    }
}
