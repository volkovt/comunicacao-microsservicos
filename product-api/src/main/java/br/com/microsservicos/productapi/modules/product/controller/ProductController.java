package br.com.microsservicos.productapi.modules.product.controller;

import br.com.microsservicos.productapi.config.exception.SuccessResponse;
import br.com.microsservicos.productapi.modules.category.dto.CategoryRequest;
import br.com.microsservicos.productapi.modules.category.dto.CategoryResponse;
import br.com.microsservicos.productapi.modules.category.service.CategoryService;
import br.com.microsservicos.productapi.modules.product.dto.ProductRequest;
import br.com.microsservicos.productapi.modules.product.dto.ProductResponse;
import br.com.microsservicos.productapi.modules.product.service.ProductService;
import br.com.microsservicos.productapi.modules.supplier.dto.SupplierRequest;
import br.com.microsservicos.productapi.modules.supplier.dto.SupplierResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ProductResponse save(@RequestBody ProductRequest productRequest) {
        return productService.save(productRequest);
    }

    @GetMapping
    public List<ProductResponse> findAll() {
        return productService.findAll();
    }

    @GetMapping("{id}")
    public ProductResponse findById(@PathVariable Integer id) {
        return productService.findByIdResponse(id);
    }

    @GetMapping("name/{name}")
    public List<ProductResponse> findByName(@PathVariable String name) {
        return productService.findByName(name);
    }

    @GetMapping("category/{categoryId}")
    public List<ProductResponse> findByCategoryId(@PathVariable Integer categoryId) {
        return productService.findByCategoryId(categoryId);
    }

    @GetMapping("supplier/{supplierId}")
    public List<ProductResponse> findBySupplierId(@PathVariable Integer supplierId) {
        return productService.findBySupplierId(supplierId);
    }

    @PutMapping("{id}")
    public ProductResponse update(@RequestBody ProductRequest productRequest,
                                   @PathVariable Integer id) {
        return productService.update(productRequest, id);
    }

    @DeleteMapping("{id}")
    public SuccessResponse delete(@PathVariable Integer id) {
        return productService.delete(id);
    }
}
