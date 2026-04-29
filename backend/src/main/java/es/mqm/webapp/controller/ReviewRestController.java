package es.mqm.webapp.controller;

import org.springframework.web.bind.annotation.RestController;

import es.mqm.webapp.dto.ReviewDTO;
import es.mqm.webapp.dto.ReviewMapper;
import es.mqm.webapp.model.Review;
import es.mqm.webapp.service.ReviewService;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewRestController {
    @Autowired
	private ReviewService reviewService;
    @Autowired
    private ReviewMapper ReviewMapper;

    @GetMapping("/")
    public Page<ReviewDTO> getReviews(Pageable pageable) {
        return reviewService.findAll(pageable).map(ReviewMapper::toDTO);
    }

    @GetMapping("/{id}")
	public ReviewDTO getReview(@PathVariable int id) {
		Review review = reviewService.findById(id).orElseThrow();
		return ReviewMapper.toDTO(review);
	}

    @PostMapping("/")
	public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) {

		Review review = ReviewMapper.toDomain(reviewDTO);
		review = reviewService.save(review);
		reviewDTO = ReviewMapper.toDTO(review);

		URI location = fromCurrentRequest().path("/{id}").buildAndExpand(reviewDTO.id()).toUri();

		return ResponseEntity.created(location).body(reviewDTO);
	}
    
    @PutMapping("/{id}")
	public ReviewDTO replaceReview(@PathVariable int id, @RequestBody ReviewDTO updatedReviewDTO) throws SQLException {

		Review updatedReview = ReviewMapper.toDomain(updatedReviewDTO);
		updatedReview = reviewService.replaceReview(id, updatedReview);
		return ReviewMapper.toDTO(updatedReview);
	}

    @DeleteMapping("/{id}")
	public ReviewDTO deleteReview(@PathVariable int id) {

		return ReviewMapper.toDTO(reviewService.deleteById(id));
	}
}
