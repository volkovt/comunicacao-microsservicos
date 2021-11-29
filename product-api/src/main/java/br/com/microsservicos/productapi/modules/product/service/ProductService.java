package br.com.microsservicos.productapi.modules.product.service;

import br.com.microsservicos.productapi.config.exception.SuccessResponse;
import br.com.microsservicos.productapi.config.exception.ValidationException;
import br.com.microsservicos.productapi.modules.category.dto.CategoryResponse;
import br.com.microsservicos.productapi.modules.category.service.CategoryService;
import br.com.microsservicos.productapi.modules.product.dto.ProductRequest;
import br.com.microsservicos.productapi.modules.product.dto.ProductResponse;
import br.com.microsservicos.productapi.modules.product.model.Product;
import br.com.microsservicos.productapi.modules.product.repository.ProductRepository;
import br.com.microsservicos.productapi.modules.supplier.dto.SupplierRequest;
import br.com.microsservicos.productapi.modules.supplier.dto.SupplierResponse;
import br.com.microsservicos.productapi.modules.supplier.model.Supplier;
import br.com.microsservicos.productapi.modules.supplier.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class ProductService {

    private static final Integer ZERO = 0;

    private ProductRepository productRepository;
    private SupplierService supplierService;
    private CategoryService categoryService;

    ProductService(ProductRepository productRepository, SupplierService supplierService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.supplierService = supplierService;
        this.categoryService = categoryService;
    }

    public ProductResponse save(ProductRequest request) {
        validateProductDataInformed(request);
        validateSupplierAndCategoryIdInformed(request);
        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());

        var product = productRepository.save(Product.of(request, category, supplier));
        return ProductResponse.of(product);
    }

    public List<ProductResponse> findAll() {
        return productRepository
                .findAll()
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findByName(String name) {
        if(isEmpty(name)) {
            throw new ValidationException("The product name must be informed.");
        }

        return productRepository
                .findByNameIgnoreCaseContaining(name)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findByCategoryId(Integer id) {
        if(isEmpty(id)) {
            throw new ValidationException("The product's category id must be informed.");
        }

        return productRepository
                .findByCategoryId(id)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findBySupplierId(Integer id) {
        if(isEmpty(id)) {
            throw new ValidationException("The product's supplier id must be informed.");
        }

        return productRepository
                .findBySupplierId(id)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public ProductResponse findByIdResponse(Integer id) {
        return ProductResponse.of(findById(id));
    }

    public Boolean existsByCategoryId(Integer categoryId) {
        return productRepository.existsByCategoryId(categoryId);
    }

    public Boolean existsBySupplierId(Integer supplierId) {
        return productRepository.existsBySupplierId(supplierId);
    }

    public Product findById(Integer id) {
        validateInformedId(id);

        return productRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("There's no product for the given id."));
    }

    public ProductResponse update(ProductRequest request,
                                  Integer id) {
        validateProductDataInformed(request);
        validateInformedId(id);
        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());
        var product = Product.of(request, category, supplier);
        product.setId(id);
        productRepository.save(product);
        return ProductResponse.of(product);
    }

    public SuccessResponse delete(Integer id) {
        validateInformedId(id);

        productRepository.deleteById(id);
        return SuccessResponse.create("The product was deleted.");
    }

    private void validateProductDataInformed(ProductRequest request) {
        if(isEmpty(request.getName())) {
            throw new ValidationException("The product's name was not informed.");
        }

        if(isEmpty(request.getQuantityAvailable())) {
            throw new ValidationException("The product's quantity was not informed.");
        }

        if(request.getQuantityAvailable() <= ZERO) {
            throw new ValidationException("The product's quantity cannot be equal to or inferior to zero.");
        }
    }

    private void validateSupplierAndCategoryIdInformed(ProductRequest request) {
        if(isEmpty(request.getCategoryId())) {
            throw new ValidationException("The category's id was not informed.");
        }

        if(isEmpty(request.getSupplierId())) {
            throw new ValidationException("The supplier's id was not informed.");
        }
    }

    private void validateInformedId(Integer id) {
        if(isEmpty(id)) {
            new ValidationException("The product id was not informed.");
        }
    }
}
