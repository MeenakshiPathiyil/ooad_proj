package dao.impl;

import dao.DBConnection;
import dao.interfaces.ReviewDAO;
import model.review.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// ReviewDAOImpl encapsulates JDBC review queries, following SRP and Pure Fabrication.
public class ReviewDAOImpl implements ReviewDAO {

    @Override
    // Stores a review in the database so higher layers do not manage SQL directly.
    public void save(Review review) {
        String sql = "INSERT INTO Review (Rating, Comment, ReviewerId, ResourceId) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, review.getRating());
            ps.setString(2, review.getComment());
            ps.setString(3, review.getReviewer().getId());
            ps.setInt(4, review.getResource().getResourceId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    // Retrieves all reviews for one resource, keeping query logic inside the DAO layer.
    public List<Review> findByResource(int resourceId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT ReviewId, Rating, Comment, ReviewerId, ResourceId FROM Review WHERE ResourceId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, resourceId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Review r = new Review(
                        rs.getInt("ReviewId"),
                        rs.getInt("Rating"),
                        rs.getString("Comment"),
                        new model.user.Student(rs.getString("ReviewerId"), "Temp", "temp@email.com", "0000000000", "pass", "Dept"),
                        new model.resource.Resource(rs.getInt("ResourceId"), "Temp", "", "", model.resource.ListingType.SELL, 0.0, null, null)
                );
                reviews.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }
}
