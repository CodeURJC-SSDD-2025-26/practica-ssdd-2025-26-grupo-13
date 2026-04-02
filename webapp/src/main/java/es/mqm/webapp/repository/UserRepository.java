package es.mqm.webapp.repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;

import es.mqm.webapp.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findBySurnames(String surnames);
    List<User> findByName(String name);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Integer id);
    long count();
    void deleteById(Integer id);

    @Query("SELECT p.category FROM Order o JOIN o.product p WHERE o.buyer.id = ?1 ORDER BY o.createdAt DESC")
    Page<String> findLastCategoriesBoughtInById(Integer id, Pageable page);

    @Query("SELECT year(u.createdAt), month(u.createdAt), count(u) " +
           "FROM User u " +
           "WHERE u.createdAt BETWEEN :start AND :end " +
           "GROUP BY year(u.createdAt), month(u.createdAt) " +
           "ORDER BY year(u.createdAt), month(u.createdAt)")
    List<Object[]> countUsersByMonthBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
