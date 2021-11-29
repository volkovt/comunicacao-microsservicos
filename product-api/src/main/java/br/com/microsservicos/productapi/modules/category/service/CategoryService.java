package br.com.microsservicos.productapi.modules.category.service;

import br.com.microsservicos.productapi.config.exception.SuccessResponse;
import br.com.microsservicos.productapi.config.exception.ValidationException;
import br.com.microsservicos.productapi.modules.category.dto.CategoryRequest;
import br.com.microsservicos.productapi.modules.category.dto.CategoryResponse;
import br.com.microsservicos.productapi.modules.category.model.Category;
import br.com.microsservicos.productapi.modules.category.repository.CategoryRepository;
import br.com.microsservicos.productapi.modules.product.service.ProductService;
import br.com.microsservicos.productapi.modules.supplier.dto.SupplierRequest;
import br.com.microsservicos.productapi.modules.supplier.dto.SupplierResponse;
import br.com.microsservicos.productapi.modules.supplier.model.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryResponse save(CategoryRequest request) {
        validateCategoryNameInformed(request);
        var category = categoryRepository.save(Category.of(request));
        return CategoryResponse.of(category);
    }

    public CategoryResponse findByIdResponse(Integer id) {
        validateInformedId(id);

        return CategoryResponse.of(findById(id));
    }

    public List<CategoryResponse> findAll() {
        return categoryRepository
                .findAll()
                .stream()
                .map(CategoryResponse::of)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> findByDescription(String description) {
        if(isEmpty(description)) {
            throw new ValidationException("The category description must be informed.");
        }

        return categoryRepository
                .findByDescriptionIgnoreCaseContaining(description)
                .stream()
                .map(CategoryResponse::of)
                .collect(Collectors.toList());
    }

    public Category findById(Integer id) {
        validateInformedId(id);

        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("There's no category for the given id."));
    }

    public CategoryResponse update(CategoryRequest request,
                                   Integer id) {
        validateCategoryNameInformed(request);
        validateInformedId(id);
        var category = Category.of(request);
        category.setId(id);
        categoryRepository.save(category);
        return CategoryResponse.of(category);
    }

    public SuccessResponse delete(Integer id) {
        validateInformedId(id);
        try {
            categoryRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException("The category cannot be delete because it was already defined by a product.");
        }
        return SuccessResponse.create("The category was deleted.");
    }

    private void validateCategoryNameInformed(CategoryRequest request) {
        if(isEmpty(request.getDescription())) {
            throw new ValidationException("The category description was not informed.");
        }
    }

    private void validateInformedId(Integer id) {
        if(isEmpty(id)) {
            new ValidationException("The category id was not informed.");
        }
    }
}
