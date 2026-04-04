package dao.impl;

import dao.DBConnection;
import dao.interfaces.ResourceDAO;
import model.resource.Resource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResourceDAOImpl implements ResourceDAO {

    @Override
    public void save(Resource resource) {
        String sql = "INSERT INTO Resource(ResourceId, Title, Description, ItemCondition, Status, ListingType, OwnerId, CategoryId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, resource.getResourceId());
            ps.setString(2, resource.getTitle());
            ps.setString(3, resource.getTitle());
            ps.setString(4, "Good");
            ps.setString(5, resource.getStatus().name());
            ps.setString(6, resource.getListingType().name());
            ps.setString(7, resource.getOwner().getId());
            ps.setInt(8, resource.getCategory().getCategoryId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Resource> findById(int id) {
        String sql = "SELECT ResourceId, Title, Description, ItemCondition, Status, ListingType, OwnerId, CategoryId FROM Resource WHERE ResourceId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Return constructed resource - full implementation depends on schema
                return Optional.of(new Resource(
                        rs.getInt("ResourceId"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("ItemCondition"),
                        null, // ListingType
                        null, // Student owner
                        null  // Category
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<Resource> findAvailableResources() {
        List<Resource> resources = new ArrayList<>();
        String sql = "SELECT ResourceId, Title, Description, ItemCondition, Status, ListingType FROM Resource WHERE Status = 'AVAILABLE'";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                resources.add(new Resource(
                        rs.getInt("ResourceId"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("ItemCondition"),
                        null, // ListingType
                        null, // Student owner
                        null  // Category
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resources;
    }

    @Override
    public void update(Resource resource) {
        String sql = "UPDATE Resource SET Title=?, Description=?, Status=? WHERE ResourceId=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, resource.getTitle());
            ps.setString(2, resource.getTitle());
            ps.setString(3, resource.getStatus().name());
            ps.setInt(4, resource.getResourceId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Resource WHERE ResourceId=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}