package br.com.microsservicos.productapi.modules.supplier.service;

import br.com.microsservicos.productapi.config.exception.SuccessResponse;
import br.com.microsservicos.productapi.config.exception.ValidationException;
import br.com.microsservicos.productapi.modules.category.dto.CategoryResponse;
import br.com.microsservicos.productapi.modules.product.dto.ProductResponse;
import br.com.microsservicos.productapi.modules.product.service.ProductService;
import br.com.microsservicos.productapi.modules.supplier.dto.SupplierRequest;
import br.com.microsservicos.productapi.modules.supplier.dto.SupplierResponse;
import br.com.microsservicos.productapi.modules.supplier.model.Supplier;
import br.com.microsservicos.productapi.modules.supplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    public SupplierResponse save(SupplierRequest request) {
        validateSupplierNameInformed(request);
        var supplier = supplierRepository.save(Supplier.of(request));
        return SupplierResponse.of(supplier);
    }

    public List<SupplierResponse> findAll() {
        return supplierRepository
                .findAll()
                .stream()
                .map(SupplierResponse::of)
                .collect(Collectors.toList());
    }

    public List<SupplierResponse> findByName(String name) {
        if(isEmpty(name)) {
            throw new ValidationException("The supplier name must be informed.");
        }

        return supplierRepository
                .findByNameIgnoreCaseContaining(name)
                .stream()
                .map(SupplierResponse::of)
                .collect(Collectors.toList());
    }

    public SupplierResponse findByIdResponse(Integer id) {
        validateInformedId(id);

        return SupplierResponse.of(findById(id));
    }

    public Supplier findById(Integer id) {
        validateInformedId(id);

        return supplierRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("There's no supplier for the given id."));
    }

    public SupplierResponse update(SupplierRequest request,
                                   Integer id) {
        validateSupplierNameInformed(request);
        validateInformedId(id);
        var supplier = Supplier.of(request);
        supplier.setId(id);
        supplierRepository.save(supplier);
        return SupplierResponse.of(supplier);
    }

    public SuccessResponse delete(Integer id) {
        validateInformedId(id);
        try{
            supplierRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException("The supplier cannot be delete because it was already defined by a product.");
        }
        return SuccessResponse.create("The supplier was deleted.");
    }

    private void validateSupplierNameInformed(SupplierRequest request) {
        if(isEmpty(request.getName())) {
            throw new ValidationException("The supplier name was not informed.");
        }
    }

    private void validateInformedId(Integer id) {
        if(isEmpty(id)) {
            new ValidationException("The supplier id was not informed.");
        }
    }
}
