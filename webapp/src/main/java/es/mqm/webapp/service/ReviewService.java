package es.mqm.webapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

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

    public Review save(Review review) {
        return repository.save(review);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public List<Review> findByProductId(Integer productId) {
        return repository.findByProductId(productId);
    }
}
