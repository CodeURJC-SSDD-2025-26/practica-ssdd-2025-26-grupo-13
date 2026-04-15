package es.mqm.webapp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import es.mqm.webapp.model.Review;
import es.mqm.webapp.model.User;

public class ReviewUtils {
     private static void addReviewStars(Map<String, Object> item, Review review) {
        int rating = Math.max(0, Math.min(5, Math.round(review.getRating())));
        item.put("star1", rating >= 1);
        item.put("star2", rating >= 2);
        item.put("star3", rating >= 3);
        item.put("star4", rating >= 4);
        item.put("star5", rating >= 5);
    }

    public static List<Map<String, Object>> getReviewsVm(Page<Review> reviewPage, User currentUser) {
        List<Map<String, Object>> reviewsVm = new ArrayList<>();
        for (Review review : reviewPage.getContent()) {
            Map<String, Object> item = new HashMap<>();
            item.put("review", review);
            boolean isUserReview = currentUser != null
                    && review.getUser() != null
                    && review.getUser().getId() == currentUser.getId();
            item.put("isUserReview", isUserReview);
            addReviewStars(item, review);
            reviewsVm.add(item);
        }
        return reviewsVm;
    }
}
