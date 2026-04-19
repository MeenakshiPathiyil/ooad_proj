package service;

import dao.interfaces.ReviewDAO;
import dao.impl.ReviewDAOImpl;
import model.review.Review;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
// ReviewService enforces review rules before persistence, following SRP and Service Layer.
public class ReviewService {

    private final ReviewDAO reviewDAO;

    public ReviewService() {
        this.reviewDAO = new ReviewDAOImpl();
    }

    // Validates the rating range before saving, keeping business checks out of controllers.
    public void submitReview(Review review) {

        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        reviewDAO.save(review);
    }

    // Retrieves reviews for one resource while keeping retrieval logic centralized in the service.
    public List<Review> getReviewsForResource(int resourceId) {
        return reviewDAO.findByResource(resourceId);
    }
}
