package dao.interfaces;

import model.user.Student;
import java.util.List;
import java.util.Optional;

// StudentDAO isolates student database access, supporting low coupling and the DAO pattern.
public interface StudentDAO {

    void save(Student student);

    Optional<Student> findById(String id);

    Optional<Student> findByEmail(String email);

    List<Student> findAll();

    void update(Student student);

    void delete(String id);
}
