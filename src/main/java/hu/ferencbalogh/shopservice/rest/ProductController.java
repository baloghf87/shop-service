package hu.ferencbalogh.shopservice.rest;

import hu.ferencbalogh.shopservice.dto.CreateProductRequest;
import hu.ferencbalogh.shopservice.dto.UpdateProductRequest;
import hu.ferencbalogh.shopservice.entity.Product;
import hu.ferencbalogh.shopservice.exception.ProductNotFoundException;
import hu.ferencbalogh.shopservice.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/product")
@Api(description = "Endpoints to manipulate products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    @ApiOperation("List all products")
    public ResponseEntity<List<Product>> list() {
        return ResponseEntity.ok(productService.list());
    }

    @PostMapping("/create")
    @ApiOperation(value = "Create a product", notes = "Returns the unique identifier of the created order")
    public ResponseEntity<Integer> create(@ApiParam("The product to create") @RequestBody @Valid CreateProductRequest request) {
        Product product = new Product(request.getName(), request.getPrice());
        productService.create(product);
        return ResponseEntity.ok(product.getId());
    }

    @PostMapping("/update")
    @ApiOperation("Update a product")
    public ResponseEntity update(@ApiParam("The product to update") @RequestBody @Valid UpdateProductRequest updateProductRequest) {
        Product product = productService.getById(updateProductRequest.getId())
                .orElseThrow(() -> new ProductNotFoundException(updateProductRequest.getId()));
        product.setName(updateProductRequest.getName());
        product.setPrice(updateProductRequest.getPrice());
        productService.update(product);
        return ResponseEntity.ok().build();
    }
}
