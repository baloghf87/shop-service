package hu.ferencbalogh.shopservice.rest;

import hu.ferencbalogh.shopservice.dto.CreateProductRequest;
import hu.ferencbalogh.shopservice.entity.Product;
import hu.ferencbalogh.shopservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public ResponseEntity<List<Product>> list() {
        return ResponseEntity.ok(productService.list());
    }

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody @Valid CreateProductRequest request) {
        Product product = new Product(request.getName(), request.getPrice());
        productService.create(product);
        return ResponseEntity.ok(product.getId());
    }

    @PostMapping("/update")
    public ResponseEntity update(@RequestBody @Valid Product product) {
        productService.update(product);
        return ResponseEntity.ok().build();
    }
}
