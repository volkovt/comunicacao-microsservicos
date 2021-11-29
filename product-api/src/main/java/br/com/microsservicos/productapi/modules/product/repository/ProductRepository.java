package br.com.microsservicos.productapi.modules.product.repository;

import br.com.microsservicos.productapi.modules.category.model.Category;
import br.com.microsservicos.productapi.modules.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByNameIgnoreCaseContaining(String name);


    List<Product> findByCategoryId(Integer id);


    List<Product> findBySupplierId(Integer category);

    Boolean existsByCategoryId(Integer id);
    Boolean existsBySupplierId(Integer id);
}
