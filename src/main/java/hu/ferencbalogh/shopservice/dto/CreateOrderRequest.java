package hu.ferencbalogh.shopservice.dto;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class CreateOrderRequest {
    @Email
    private String buyerEmail;

    @NotNull
    @Size(min = 1)
    @Valid
    private List<Item> items;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(String buyerEmail, List<Item> items) {
        this.buyerEmail = buyerEmail;
        this.items = items;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Item {
        @Min(0)
        private int productId;

        @Min(1)
        private int quantity;

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
