package hu.ferencbalogh.shopservice.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CreateProductRequest {
    @NotNull
    private String name;

    @NotNull
    private BigDecimal price;

    public CreateProductRequest() {
    }

    public CreateProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
