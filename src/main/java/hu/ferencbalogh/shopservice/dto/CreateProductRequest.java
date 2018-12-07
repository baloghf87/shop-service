package hu.ferencbalogh.shopservice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel(description = "Class representing an product to create")
public class CreateProductRequest {
    @NotNull
    @ApiModelProperty(notes = "The name of the product", required = true)
    private String name;

    @NotNull
    @ApiModelProperty(notes = "The price of the product", required = true)
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

    @Override
    public String toString() {
        return "CreateProductRequest{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
