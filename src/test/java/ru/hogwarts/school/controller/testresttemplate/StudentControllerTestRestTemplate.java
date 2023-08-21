package ru.hogwarts.school.controller.testresttemplate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.HogwartsApplication;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.reposirory.AvatarRepository;
import ru.hogwarts.school.reposirory.FacultyRepository;
import ru.hogwarts.school.reposirory.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = HogwartsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTestRestTemplate {


    public static final Student IVAN = new Student(null, "Ivan", 20);
    public static final Student SERGEY = new Student(null, "Sergey", 25);
    @Autowired
    TestRestTemplate template;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    FacultyRepository facultyRepository;
    @Autowired
    AvatarRepository avatarRepository;
    @Autowired
    StudentService studentService;
    @Autowired
    FacultyService facultyService;


    @BeforeEach
    void init() {
        template.postForEntity("/student", IVAN, Student.class);
        template.postForEntity("/student", SERGEY, Student.class);
    }

    @Autowired
    void clearDB() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    private ResponseEntity<Student> createStudent(String name, int age) {
        Student request = new Student();
        request.setName(name);
        request.setAge(age);
        ResponseEntity<Student> response = template.postForEntity("/student", request, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        return response;

    }


    @Test
    void create() {
        ResponseEntity<Student> response = createStudent("Oleg", 21);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Oleg");
        assertThat(response.getBody().getAge()).isEqualTo(21);
    }

    @Test
    void getById() {
        ResponseEntity<Student> student = createStudent("Oleg", 21);
        Long id = student.getBody().getId();
        ResponseEntity<Student> response = template.getForEntity("/student/" + id, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(id);
        assertThat(response.getBody().getName()).isEqualTo("Oleg");
        assertThat(response.getBody().getAge()).isEqualTo(21);
    }

    @Test
    void getAll() {
        ResponseEntity<Collection> response = template.getForEntity("/student", Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        Collection<Student> body = response.getBody();
        assertThat(body.isEmpty()).isFalse();
        assertThat(body.size()).isEqualTo(2);

    }

    @Test
    void update() {
        ResponseEntity<Student> response = createStudent("Oleg", 21);
        Student student = response.getBody();
        student.setAge(25);
        template.put("/student/" + student.getId(), student);
        response = template.getForEntity("/student/" + student.getId(), Student.class);
        assertThat(response.getBody().getAge()).isEqualTo(25);

    }

    @Test
    void delete() {
        ResponseEntity<Student> response = createStudent("Oleg", 21);
        template.delete("/student/" + response.getBody().getId());
        response = template.getForEntity("/student/" + response.getBody().getId(), Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void filtered() {
        ResponseEntity<Collection> response = template
                .getForEntity("/student/filtered?age=25", Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(1);
        assertThat(response.getBody()).isNotNull();


    }

    @Test
    void ageBetween() {
        ResponseEntity<Collection> response = template
                .getForEntity("/student/age-between?min=21&max=30", Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(1);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void byFaculty() {
        Faculty faculty = new Faculty(null, "Fiz", "red");
        ResponseEntity<Faculty> response = template.postForEntity("/faculty", faculty, Faculty.class);
        Student student = new Student(null, "Vova", 20);
        student.setFaculty(response.getBody());
        ResponseEntity<Student> studentResponseEntity = template.postForEntity("/student", student, Student.class);
        Long facultyId = response.getBody().getId();
        ResponseEntity<Collection> students = template.getForEntity("/student/by-faculty?facultyId=" + facultyId, Collection.class);
        assertThat(students.getBody().isEmpty()).isFalse();
        Map<String, String> resultStudent = (LinkedHashMap<String, String>) students.getBody().iterator().next();
        assertThat(resultStudent.get("name")).isEqualTo("Vova");

    }

    @Test
    public void getCount() {
        ResponseEntity<Long> count = template.getForEntity("/student/count", Long.class);
        assertThat(count.getBody()).isEqualTo(2);
    }

    @Test
    public void getAverage() {
        ResponseEntity<Double> average = template.getForEntity("/student/average-age", Double.class);
        assertThat(average.getBody()).isNotNull();
        assertThat(average.getBody()).isEqualTo(22.5);
    }

    @Test
    public void getLastFive() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(null, "Student3", 20));
        students.add(new Student(null, "Student4", 21));
        students.add(new Student(null, "Student5", 22));
        students.add(new Student(null, "Student6", 23));
        students.add(new Student(null, "Student7", 24));
        students.add(new Student(null, "Student8", 25));
        students.add(new Student(null, "Student9", 26));
        students.add(new Student(null, "Student10", 27));
        for (Student student : students) {
            template.postForEntity("/student", student, Student.class);
        }
        ResponseEntity<Student[]> response = template.getForEntity("/student/last-five", Student[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(5);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(5);
        assertThat(response.getBody()[0].getId()).isEqualTo(10L);
        assertThat(response.getBody()[1].getId()).isEqualTo(9L);
        assertThat(response.getBody()[2].getId()).isEqualTo(8L);
        assertThat(response.getBody()[3].getId()).isEqualTo(7L);
        assertThat(response.getBody()[4].getId()).isEqualTo(6L);
    }

}





