package dao.impl;

import dao.DBConnection;
import dao.interfaces.ReminderDAO;
import model.reminder.Reminder;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReminderDAOImpl implements ReminderDAO {

    @Override
    public int save(Reminder reminder, String studentId, Integer transactionId) {
        String sql = "INSERT INTO Reminder (Message, Status, ReminderDate, StudentId, TransactionId) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, reminder.getMessage());
            ps.setString(2, reminder.getStatus());
            ps.setDate(3, Date.valueOf(reminder.getReminderDate()));
            ps.setString(4, studentId);
            if (transactionId == null) {
                ps.setNull(5, Types.INTEGER);
            } else {
                ps.setInt(5, transactionId);
            }

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
    public List<Reminder> findByStudent(String studentId) {
        List<Reminder> res = new ArrayList<>();
        String sql = "SELECT ReminderId, Message, Status, ReminderDate FROM Reminder WHERE StudentId = ? ORDER BY ReminderDate DESC, ReminderId DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("ReminderId");
                    String msg = rs.getString("Message");
                    String status = rs.getString("Status");
                    Date d = rs.getDate("ReminderDate");
                    LocalDate date = d != null ? d.toLocalDate() : LocalDate.now();
                    res.add(new Reminder(id, msg, status, date));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

    @Override
    public void markRead(int reminderId) {
        String sql = "UPDATE Reminder SET Status = 'READ' WHERE ReminderId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reminderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

