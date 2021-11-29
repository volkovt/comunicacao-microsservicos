package br.com.microsservicos.productapi.modules.supplier.repository;

import br.com.microsservicos.productapi.modules.category.model.Category;
import br.com.microsservicos.productapi.modules.supplier.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    List<Supplier> findByNameIgnoreCaseContaining(String name);
}
