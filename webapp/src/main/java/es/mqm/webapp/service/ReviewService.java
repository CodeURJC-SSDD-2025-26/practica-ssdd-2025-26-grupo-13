package es.mqm.webapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.Review;
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
    public List<Review> findByUserDest(int id){
        List<Review> reviews = repository.findAll();
        reviews.removeIf(r -> r.getProduct() == null
                || r.getProduct().getUser() == null
                || r.getProduct().getUser().getId() != id);
        return reviews;
    }

    public Review save(Review review) {
        return repository.save(review);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public List<Review> findByProductId(Integer productId) {
        return repository.findByProductId(productId);
    }
    public Page<Review> findByProductId(int productId, Pageable pageable) {
        return repository.findByProductId(productId, pageable);
    }
    
}
