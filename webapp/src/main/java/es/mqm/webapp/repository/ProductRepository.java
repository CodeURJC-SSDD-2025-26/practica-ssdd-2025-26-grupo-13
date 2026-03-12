package es.mqm.webapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import es.mqm.webapp.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByName(String name);

    List<Product> findByNameIgnoreCase(String name);

    List<Product> findByCategoriesContaining(String category);

    List<Product> findBySeller(String seller);

    List<Product> findAll();

    Optional<Product> findById(Integer id);

    // Pageable overloads — filtering + pagination done at the DB level
    Page<Product> findAll(Pageable pageable);

    Page<Product> findByNameIgnoreCase(String name, Pageable pageable);

    Page<Product> findByCategoriesContaining(String category, Pageable pageable);
}