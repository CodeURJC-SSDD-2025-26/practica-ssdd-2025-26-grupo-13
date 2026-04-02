package es.mqm.webapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Optional<Review> findById(Integer id);
    List<Review> findByProductId(Integer productId);
    List<Review> findByUserId(Integer userId);
    List<Review> findByProductUserId(Integer userId);
    void deleteById(Integer id);
    List<Review> findByRating(float rating);

    Page<Review> findByProductId(int productId, Pageable pageable);
}