package br.com.microsservicos.productapi.modules.category.controller;

import br.com.microsservicos.productapi.config.exception.SuccessResponse;
import br.com.microsservicos.productapi.modules.category.dto.CategoryRequest;
import br.com.microsservicos.productapi.modules.category.dto.CategoryResponse;
import br.com.microsservicos.productapi.modules.category.model.Category;
import br.com.microsservicos.productapi.modules.category.service.CategoryService;
import br.com.microsservicos.productapi.modules.product.dto.ProductRequest;
import br.com.microsservicos.productapi.modules.product.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public CategoryResponse save(@RequestBody CategoryRequest categoryRequest) {
        return categoryService.save(categoryRequest);
    }

    @GetMapping
    public List<CategoryResponse> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("{id}")
    public CategoryResponse findById(@PathVariable Integer id) {
        return categoryService.findByIdResponse(id);
    }

    @GetMapping("description/{description}")
    public List<CategoryResponse> findByDescription(@PathVariable String description) {
        return categoryService.findByDescription(description);
    }

    @PutMapping("{id}")
    public CategoryResponse update(@RequestBody CategoryRequest categoryRequest,
                                  @PathVariable Integer id) {
        return categoryService.update(categoryRequest, id);
    }

    @DeleteMapping("{id}")
    public SuccessResponse delete(@PathVariable Integer id) {
        return categoryService.delete(id);
    }
}
