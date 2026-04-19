package controller;

import model.review.Review;
import service.ReviewService;

import java.util.List;

// ReviewController accepts legacy review requests and delegates them, following GRASP Controller.
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController() {
        this.reviewService = new ReviewService();
    }

    // Passes review submission to the service layer so validation stays out of the UI.
    public void submitReview(Review review) {
        reviewService.submitReview(review);
        System.out.println("[INFO] Review submitted.");
    }

    // Retrieves reviews for one resource while keeping menu classes simple.
    public List<Review> getReviewsForResource(int resourceId) {
        return reviewService.getReviewsForResource(resourceId);
    }
}
