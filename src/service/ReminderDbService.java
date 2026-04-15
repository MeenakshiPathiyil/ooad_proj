package service;

import dao.impl.ReminderDAOImpl;
import dao.interfaces.ReminderDAO;
import model.reminder.Reminder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReminderDbService {

    private final ReminderDAO reminderDAO;

    public ReminderDbService() {
        this.reminderDAO = new ReminderDAOImpl();
    }

    public int createReminder(String studentId, Integer transactionId, String message, LocalDate reminderDate) {
        Reminder r = new Reminder(0, message, "UNREAD", reminderDate);
        return reminderDAO.save(r, studentId, transactionId);
    }

    public List<Reminder> getRemindersForStudent(String studentId) {
        return reminderDAO.findByStudent(studentId);
    }

    public void markRead(int reminderId) {
        reminderDAO.markRead(reminderId);
    }
}

