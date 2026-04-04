package dao.impl;

import dao.DBConnection;
import dao.interfaces.ReviewDAO;
import model.review.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAOImpl implements ReviewDAO {

    @Override
    public void save(Review review) {
        String sql = "INSERT INTO Review (ReviewId, Rating, Comment, ReviewerId, ResourceId) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, 0); // ReviewId auto-generated
            ps.setInt(2, review.getRating());
            ps.setString(3, review.getComment());
            ps.setString(4, "reviewer_id"); // Get from review object when fully implemented
            ps.setInt(5, 0); // ResourceId - get from review object

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Review> findByResource(int resourceId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT ReviewId, Rating, Comment, ReviewerId, ResourceId FROM Review WHERE ResourceId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, resourceId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // Return basic review info
                // Full implementation would reconstruct complete Review objects
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }
}