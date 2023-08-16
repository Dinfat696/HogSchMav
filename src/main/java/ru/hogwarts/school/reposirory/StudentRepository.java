package ru.hogwarts.school.reposirory;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByAge(int age);

    List<Student> findAllByAgeBetween(int min, int max);
    @Query(value = "SELECT COUNT(*) FROM Student",nativeQuery = true)
    Long getCountStudents();
    @Query(value = "SELECT AVG(age) FROM Student",nativeQuery = true)
    Double getAverageAgeStudents();
    @Query(value = "SELECT * FROM Student ORDER BY id DESC LIMIT 5",nativeQuery = true)
    List<Student> findLastFiveStudents();

}