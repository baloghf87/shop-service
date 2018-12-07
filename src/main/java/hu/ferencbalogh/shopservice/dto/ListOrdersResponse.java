package hu.ferencbalogh.shopservice.dto;

import hu.ferencbalogh.shopservice.entity.Order;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApiModel(description = "Class representing an order")
public class ListOrdersResponse {
    @ApiModelProperty(notes = "The unique identifier of the order", example = "1")
    private Integer id;

    @ApiModelProperty(notes = "The e-mail address ofd the buyer", example = "test123@gmail.com")
    private String buyerEmail;

    @ApiModelProperty(notes = "The time the order was submitted", example = "2018-12-05T12:05:33.441+01:00")
    private ZonedDateTime orderTime;

    @ApiModelProperty(notes = "The ordered products")
    private List<OrderListItem> items;

    @ApiModelProperty(notes = "The total price of the products", example = "123.45")
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

    public ListOrdersResponse(Integer id, String buyerEmail, ZonedDateTime orderTime, List<OrderListItem> items, BigDecimal total) {
        this.id = id;
        this.buyerEmail = buyerEmail;
        this.orderTime = orderTime;
        this.items = items;
        this.total = total;
    }

    private void calculateTotal() {
        total = getItems().stream()
                .map(item -> item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<OrderListItem> getItems(Order order) {
        return order.getItems().stream()
                .map(orderItem -> new OrderListItem(
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

    public List<OrderListItem> getItems() {
        return items;
    }

    public void setItems(List<OrderListItem> items) {
        this.items = items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
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

    @ApiModel(description = "Class representing a product in an order")
    public static class OrderListItem {
        @ApiModelProperty(notes = "The unique identifier of the product", example = "1")
        private Integer id;

        @ApiModelProperty(notes = "The name identifier of the product", example = "USB stick")
        private String name;

        @ApiModelProperty(notes = "The price of the product", example = "123.45")
        private BigDecimal unitPrice;

        @ApiModelProperty(notes = "The ordered quantity", example = "1")
        private Integer quantity;

        public OrderListItem() {
        }

        public OrderListItem(Integer id, String name, BigDecimal unitPrice, Integer quantity) {
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
}
