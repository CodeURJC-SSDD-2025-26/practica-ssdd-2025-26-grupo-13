package es.mqm.webapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import es.mqm.webapp.model.Order;
import es.mqm.webapp.model.User;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findById(Integer id);
    List<Order> findByBuyer(User buyer);
    List<Order> findAll();
    Integer countByBuyer(User buyer);
    Integer countByProductUser(User seller);

    Page<Order> findByBuyer(User buyer, Pageable pageable);
}
