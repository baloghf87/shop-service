package hu.ferencbalogh.shopservice.dto;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class CreateOrderRequest {
    @NotNull
    @Email
    private String buyerEmail;

    @NotNull
    @Size(min = 1)
    @Valid
    private List<CreateOrderItem> items;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(String buyerEmail, List<CreateOrderItem> items) {
        this.buyerEmail = buyerEmail;
        this.items = items;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public List<CreateOrderItem> getItems() {
        return items;
    }

    public void setItems(List<CreateOrderItem> items) {
        this.items = items;
    }

    public static class CreateOrderItem {
        @NotNull
        @Min(0)
        private Integer productId;

        @NotNull
        @Min(1)
        private Integer quantity;

        public CreateOrderItem() {
        }

        public CreateOrderItem(Integer productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}
