package es.mqm.webapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import es.mqm.webapp.model.User;
import es.mqm.webapp.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByName(String name);
    List<Product> findByCategory(String category);
    List<Product> findByUser(User user);
    List<Product> findAll();
    Optional<Product> findById(Integer id);

}