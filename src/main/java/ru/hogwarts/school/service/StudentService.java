package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.reposirory.AvatarRepository;
import ru.hogwarts.school.reposirory.FacultyRepository;
import ru.hogwarts.school.reposirory.StudentRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Service
public class StudentService {
    @Autowired
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final AvatarRepository avatarRepository;


    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.avatarRepository = avatarRepository;
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

    @Transactional
    public Student remove(Long id) {
        avatarRepository.deleteByStudent_id(id);
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

    public Long getCountStudent() {
        return studentRepository.getCountStudents();
    }

    public Double getAverageAgeStudents() {
        return studentRepository.getAverageAgeStudents();
    }

    public List<Student> findLastFiveStudents() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<Student> studentsPage = studentRepository.findLastFiveStudents(pageRequest);
        return studentsPage.getContent();

    }
}
