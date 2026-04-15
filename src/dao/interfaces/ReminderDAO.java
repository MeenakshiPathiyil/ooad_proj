package dao.interfaces;

import model.reminder.Reminder;

import java.util.List;

public interface ReminderDAO {
    int save(Reminder reminder, String studentId, Integer transactionId);

    List<Reminder> findByStudent(String studentId);

    void markRead(int reminderId);
}

