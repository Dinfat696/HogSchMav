package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.reposirory.FacultyRepository;
import ru.hogwarts.school.reposirory.StudentRepository;

import java.util.Collection;

@Service
public class StudentService {
    @Autowired
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;


    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student create(Student student) {
        return studentRepository.save(student);
    }

    public Student update(Long id, Student student) {
        Student existStudent = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        existStudent.setAge(student.getAge());
        existStudent.setName(student.getName());
        return studentRepository.save(student);
    }

    public Student getById(Long id) {
        return studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);

    }

    public Collection<Student> getAll() {
        return studentRepository.findAll();
    }

    public Student remove(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        studentRepository.delete(student);
        return student;
    }


    public Collection<Student> getAllByAge(int age) {
        return studentRepository.findAllByAge(age);

    }

    public Collection<Student> getByAge(int min, int max) {
        return studentRepository.findAllByAgeBetween(min, max);
    }

    public Collection<Student> getByFacultyId(Long facultyId) {
        return facultyRepository.findById(facultyId)
                .map(Faculty::getStudents)
                .orElseThrow(FacultyNotFoundException::new);
    }
}
