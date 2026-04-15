package dao.impl;

import dao.DBConnection;
import dao.interfaces.TransactionDAO;
import model.transaction.BarterTransaction;
import model.transaction.BuySellTransaction;
import model.transaction.LendBorrowTransaction;
import model.transaction.StoredTransaction;
import model.transaction.Transaction;
import model.transaction.TransactionStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOImpl implements TransactionDAO {

    @Override
    public int createTransaction(String transactionType, String status) {
        String sql = "INSERT INTO `Transaction` (TransactionType, Status) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, transactionType);
            ps.setString(2, status);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void insertBuySell(int transactionId, int resourceId, String sellerId, String buyerId, double price) {
        String sql = "INSERT INTO BuySellTransaction (TransactionId, ResourceId, SellerId, BuyerId, Price) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, transactionId);
            ps.setInt(2, resourceId);
            ps.setString(3, sellerId);
            ps.setString(4, buyerId);
            ps.setDouble(5, price);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertLendBorrow(int transactionId, int resourceId, String lenderId, String borrowerId,
                                 java.time.LocalDate startDate, java.time.LocalDate endDate, double penalty) {
        String sql = "INSERT INTO LendBorrowTransaction (TransactionId, ResourceId, LenderId, BorrowerId, StartDate, EndDate, Penalty) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, transactionId);
            ps.setInt(2, resourceId);
            ps.setString(3, lenderId);
            ps.setString(4, borrowerId);
            ps.setDate(5, Date.valueOf(startDate));
            ps.setDate(6, Date.valueOf(endDate));
            ps.setDouble(7, penalty);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT TransactionId, TransactionType, Status, CreatedAt FROM `Transaction`";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("TransactionId");
                String type = rs.getString("TransactionType");
                String statusStr = rs.getString("Status");
                Timestamp createdAtTs = rs.getTimestamp("CreatedAt");

                TransactionStatus status = TransactionStatus.valueOf(statusStr);
                transactions.add(new StoredTransaction(
                        id,
                        type,
                        status,
                        createdAtTs != null ? createdAtTs.toLocalDateTime() : null
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    @Override
    public void updateStatus(int transactionId, String status) {
        String sql = "UPDATE `Transaction` SET Status = ? WHERE TransactionId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, transactionId);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<dao.dto.BorrowedItem> findBorrowedByBorrower(String borrowerId) {
        List<dao.dto.BorrowedItem> res = new ArrayList<>();
        String sql = """
                SELECT t.TransactionId, t.Status, lb.ResourceId, r.Title, lb.LenderId, lb.StartDate, lb.EndDate
                FROM `Transaction` t
                JOIN LendBorrowTransaction lb ON lb.TransactionId = t.TransactionId
                JOIN Resource r ON r.ResourceId = lb.ResourceId
                WHERE lb.BorrowerId = ?
                ORDER BY t.TransactionId DESC
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, borrowerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    res.add(new dao.dto.BorrowedItem(
                            rs.getInt("TransactionId"),
                            rs.getInt("ResourceId"),
                            rs.getString("Title"),
                            rs.getString("Status"),
                            rs.getString("LenderId"),
                            rs.getDate("StartDate").toLocalDate(),
                            rs.getDate("EndDate").toLocalDate()
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

    @Override
    public List<dao.dto.BoughtItem> findBoughtByBuyer(String buyerId) {
        List<dao.dto.BoughtItem> res = new ArrayList<>();
        String sql = """
                SELECT t.TransactionId, t.Status, b.ResourceId, r.Title, b.SellerId, b.Price
                FROM `Transaction` t
                JOIN BuySellTransaction b ON b.TransactionId = t.TransactionId
                JOIN Resource r ON r.ResourceId = b.ResourceId
                WHERE b.BuyerId = ?
                ORDER BY t.TransactionId DESC
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, buyerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    res.add(new dao.dto.BoughtItem(
                            rs.getInt("TransactionId"),
                            rs.getInt("ResourceId"),
                            rs.getString("Title"),
                            rs.getString("Status"),
                            rs.getString("SellerId"),
                            rs.getDouble("Price")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

    @Override
    public void completeLendBorrow(int transactionId) {
        // Update Transaction status + mark Resource AVAILABLE.
        String select = "SELECT ResourceId FROM LendBorrowTransaction WHERE TransactionId = ?";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            Integer resourceId = null;
            try (PreparedStatement ps = conn.prepareStatement(select)) {
                ps.setInt(1, transactionId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        resourceId = rs.getInt("ResourceId");
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("UPDATE `Transaction` SET Status='COMPLETED' WHERE TransactionId=?")) {
                ps.setInt(1, transactionId);
                ps.executeUpdate();
            }

            if (resourceId != null) {
                try (PreparedStatement ps = conn.prepareStatement("UPDATE Resource SET Status='AVAILABLE' WHERE ResourceId=?")) {
                    ps.setInt(1, resourceId);
                    ps.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
