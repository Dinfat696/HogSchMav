package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.stream.Collectors;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
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
        logger.info("Start method create");
        return studentRepository.save(student);
    }

    public Student update(Long id, Student student) {
        logger.info("Start method update");
        Student existStudent = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        existStudent.setAge(student.getAge());
        existStudent.setName(student.getName());
        return studentRepository.save(student);
    }

    public Student getById(Long id) {
        logger.info("Start method getById");
        return studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);

    }

    public Collection<Student> getAll() {
        logger.info("Start method getAll");
        return studentRepository.findAll();
    }

    @Transactional
    public Student remove(Long id) {
        logger.info("Start method remove");
        avatarRepository.deleteByStudent_id(id);
        Student student = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        studentRepository.delete(student);
        return student;
    }


    public Collection<Student> getAllByAge(int age) {
        logger.info("Start method getAllByAge");
        return studentRepository.findAllByAge(age);

    }

    public Collection<Student> getByAge(int min, int max) {
        logger.info("Start method getByAge");
        return studentRepository.findAllByAgeBetween(min, max);
    }

    public Collection<Student> getByFacultyId(Long facultyId) {
        logger.info("Start method getByFacultyId");
        return facultyRepository.findById(facultyId)
                .map(Faculty::getStudents)
                .orElseThrow(FacultyNotFoundException::new);
    }

    public Long getCountStudent() {
        logger.info("Start method getCountStudent");
        return studentRepository.getCountStudents();
    }

    public Double getAverageAgeStudents() {
        logger.info("Start method getAverageAgeStudent");
        return studentRepository.getAverageAgeStudents();
    }

    public List<Student> findLastFiveStudents() {
        logger.info("Start method findLastFiveStudents");
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<Student> studentsPage = studentRepository.findLastFiveStudents(pageRequest);
        return studentsPage.getContent();

    }

    public List<String> getNameStartedBy(char firstSymbol) {
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(n -> Character.toLowerCase(n.charAt(0))
                        == Character.toLowerCase(firstSymbol))
                .collect(Collectors.toList());
    }

    public double getAverageAge() {
        return studentRepository.findAll()
                .stream()
                .mapToInt(Student::getAge)
                .average()
                .orElseThrow(StudentNotFoundException::new);
    }
}
