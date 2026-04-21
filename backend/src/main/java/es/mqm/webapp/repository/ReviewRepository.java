package es.mqm.webapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.mqm.webapp.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Optional<Review> findById(Integer id);
    Optional<Review> findByProductId(Integer productId);
    List<Review> findByUserId(Integer userId);
    List<Review> findByProductUserId(Integer userId);
    List<Review> findByRating(float rating);

    Page<Review> findByProductUserId(Integer userId, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.user.id = :userId")
    Double findAverageRatingByProductUserId(Integer userId);
}
