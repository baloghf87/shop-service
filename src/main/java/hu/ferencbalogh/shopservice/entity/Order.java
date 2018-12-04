package hu.ferencbalogh.shopservice.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @Email
    private String buyerEmail;

    @NotNull
    private ZonedDateTime orderTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> items;

    public Order() {
    }

    public Order(String buyerEmail, ZonedDateTime orderTime, List<OrderItem> items) {
        this.buyerEmail = buyerEmail;
        this.orderTime = orderTime;
        setItems(items);
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
        items.forEach(item -> item.setOrder(this));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                Objects.equals(buyerEmail, order.buyerEmail) &&
                Objects.equals(orderTime, order.orderTime) &&
                Objects.equals(new ArrayList<>(items), new ArrayList<>(order.items));
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
