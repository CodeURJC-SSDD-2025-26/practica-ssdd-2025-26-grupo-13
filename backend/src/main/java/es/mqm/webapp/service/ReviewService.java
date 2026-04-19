package es.mqm.webapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import es.mqm.webapp.model.Review;
import es.mqm.webapp.model.User;
import es.mqm.webapp.repository.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
	private ReviewRepository repository;

    public List<Review> findAll() {
        return repository.findAll();
    }
    
    public Page<Review> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<Review> findById(int id) {
        return repository.findById(id);
    }
    public List<Review> findByUserId(int id){
        return repository.findByUserId(id);
    }
    public List<Review> findByRating(float rating){
        return repository.findByRating(rating);
    }
    public List<Review> findByUserDest(int id){
        return repository.findByProductUserId(id);
    }

    public Review save(Review review) {
        return repository.save(review);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
    public void deleteByProductId(Integer id) {
        repository.deleteById(id);
    }

    public Optional<Review> findByProductId(Integer productId) {
        return repository.findByProductId(productId);
    }

    public Page<Review> findByProductUserId(Integer userId, Pageable pageable) {
        return repository.findByProductUserId(userId, pageable);
    }

    public double findAverageRatingByProductUserId(Integer userId) {
        Double average = repository.findAverageRatingByProductUserId(userId);
        return average != null ? average : 0.0;
    }

    public boolean isUserOrAdmin(int id, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        Optional<Review> reviewOpt = repository.findById(id);
        if (!reviewOpt.isPresent()) {
            return false;
        }
        Review review = reviewOpt.get();
        User user = review.getUser();
        boolean isUser = user.getEmail().equals(auth.getName());
        return isUser || isAdmin;
    }
    
}
