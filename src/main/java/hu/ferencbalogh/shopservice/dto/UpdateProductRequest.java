package hu.ferencbalogh.shopservice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel(description = "Class representing a product update")
public class UpdateProductRequest {
    @NotNull
    @ApiModelProperty(notes = "The unique identifier of the product to update", example = "1")
    private Integer id;

    @NotNull
    @ApiModelProperty(notes = "The new name of the product", example = "USB stick")
    private String name;

    @NotNull
    @ApiModelProperty(notes = "The new price of the product", example = "123.45")
    private BigDecimal price;

    public UpdateProductRequest() {
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "UpdateProductRequest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
