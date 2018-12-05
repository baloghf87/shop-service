package hu.ferencbalogh.shopservice.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class OrderItem {
    @Id
    @GeneratedValue(generator = "order_item_generator")
    @SequenceGenerator(name = "order_item_generator", sequenceName = "order_item_seq")
    private Integer id;

    @NotNull
    @ManyToOne
    private Product product;

    @NotNull
    private BigDecimal unitPrice;

    @NotNull
    private Integer quantity;

    public OrderItem() {
    }

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.unitPrice = product.getPrice();
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id) &&
                Objects.equals(product, orderItem.product) &&
                Objects.equals(unitPrice, orderItem.unitPrice) &&
                Objects.equals(quantity, orderItem.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, unitPrice, quantity);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", product=" + product +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                '}';
    }
}
