package es.mqm.webapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.User;

public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    List<Product> findByName(String name);

    List<Product> findByNameIgnoreCase(String name);

    List<Product> findByNameIgnoreCaseContaining(String name);

    List<Product> findByCategory(String category);

    List<Product> findByUser(User user);

    List<Product> findAll();

    Optional<Product> findById(Integer id);

    Page<Product> findAll(Pageable pageable);

    Page<Product> findByNameIgnoreCase(String name, Pageable pageable);

    Page<Product> findByNameIgnoreCaseContaining(String name, Pageable pageable);

    Page<Product> findByCategory(String category, Pageable pageable);

    List<Product> findByIsSoldFalse();

    int countByCategory(String category);
    Page<Product> findByIsSoldFalse(Pageable pageable);

    Page<Product> findByIsSoldFalseAndUser(Pageable pageable, User user);

    long count();

    List<Product> findByIsSoldFalseAndUser(User user);

}