package dao.impl;

import dao.DBConnection;
import dao.interfaces.ResourceDAO;
import model.resource.*;
import model.user.Student;

import java.sql.*;
import java.util.*;

// ResourceDAOImpl handles java database connectivity (JDBC) persistence for resources, following SRP and Pure Fabrication.
public class ResourceDAOImpl implements ResourceDAO {

    private static Boolean hasPriceColumnCache;

    @Override
    // Persists a new resource while hiding SQL details from services, supporting low coupling.
    public void save(Resource resource) {

        String sqlWithPrice = "INSERT INTO Resource (Title, Description, ItemCondition, Status, ListingType, Price, OwnerId, CategoryId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlNoPrice = "INSERT INTO Resource (Title, Description, ItemCondition, Status, ListingType, OwnerId, CategoryId) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(hasPriceColumn(conn) ? sqlWithPrice : sqlNoPrice)) {

            ps.setString(1, resource.getTitle());
            ps.setString(2, resource.getDescription());
            ps.setString(3, resource.getCondition());
            ps.setString(4, resource.getStatus().name());
            ps.setString(5, resource.getListingType().name());

            if (hasPriceColumn(conn)) {
                ps.setDouble(6, resource.getPrice());
                ps.setString(7, resource.getOwner().getId());
                ps.setInt(8, resource.getCategory().getCategoryId());
            } else {
                ps.setString(6, resource.getOwner().getId());
                ps.setInt(7, resource.getCategory().getCategoryId());
            }

            ps.executeUpdate();

            System.out.println("Resource added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    // Fetches one resource by id so higher layers stay independent from SQL.
    public Optional<Resource> findById(int id) {

        String sql = "SELECT * FROM Resource WHERE ResourceId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResource(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    // Returns only available resources, keeping query-specific persistence logic inside the DAO.
    public List<Resource> findAvailableResources() {

        List<Resource> resources = new ArrayList<>();

        String sql = "SELECT * FROM Resource WHERE Status = 'AVAILABLE'";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                resources.add(mapResource(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resources;
    }

    @Override
    // Updates stored resource data after business logic changes it in memory.
    public void update(Resource resource) {

        String sqlWithPrice = "UPDATE Resource SET Title=?, Description=?, Status=?, Price=? WHERE ResourceId=?";
        String sqlNoPrice = "UPDATE Resource SET Title=?, Description=?, Status=? WHERE ResourceId=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(hasPriceColumn(conn) ? sqlWithPrice : sqlNoPrice)) {

            ps.setString(1, resource.getTitle());
            ps.setString(2, resource.getDescription());
            ps.setString(3, resource.getStatus().name());
            if (hasPriceColumn(conn)) {
                ps.setDouble(4, resource.getPrice());
                ps.setInt(5, resource.getResourceId());
            } else {
                ps.setInt(4, resource.getResourceId());
            }

            ps.executeUpdate();

            System.out.println("Resource updated!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    // Deletes a resource by id while keeping database logic out of controllers and services.
    public void delete(int id) {

        String sql = "DELETE FROM Resource WHERE ResourceId=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            System.out.println(" Resource deleted!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Maps a database row into a Resource object, centralizing object reconstruction logic.
    private Resource mapResource(ResultSet rs) throws SQLException {

        Student owner = new Student(
                rs.getString("OwnerId"),
                "Temp", "temp@email.com", "0000000000", "pass", "Dept"
        );

        Category category = new Category(
            rs.getInt("CategoryId"),
            "Temp",
            "Temp"
        );

        double price = 0.0;
        try {
            price = rs.getDouble("Price");
        } catch (SQLException ignored) {
        }

        return new Resource(
                rs.getInt("ResourceId"),
                rs.getString("Title"),
                rs.getString("Description"),
                rs.getString("ItemCondition"),
                ListingType.valueOf(rs.getString("ListingType")),
                price,
                owner,
                category
        );
    }

    private boolean hasPriceColumn(Connection conn) {
        if (hasPriceColumnCache != null) return hasPriceColumnCache;
        try {
            DatabaseMetaData md = conn.getMetaData();
            try (ResultSet cols = md.getColumns(conn.getCatalog(), null, "Resource", "Price")) {
                hasPriceColumnCache = cols.next();
            }
        } catch (SQLException e) {
            hasPriceColumnCache = false;
        }
        return hasPriceColumnCache;
    }
}
