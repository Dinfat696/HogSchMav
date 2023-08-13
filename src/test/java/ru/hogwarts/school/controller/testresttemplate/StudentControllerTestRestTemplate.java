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
import ru.hogwarts.school.reposirory.FacultyRepository;
import ru.hogwarts.school.reposirory.StudentRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = HogwartsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTestRestTemplate {


    public static final Student IVAN = new Student(null, "Ivan", 20);
    public static final Student SERGEY = new Student(null, "Sergey", 25);
    @Autowired
    TestRestTemplate template;
    @Autowired
    FacultyRepository facultyRepository;
    @Autowired
    StudentRepository studentRepository;

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
//    @Test
//    void byFaculty(){
//        ResponseEntity<Collection> response;
//        response = template.getForEntity("/student", Collection.class);
//        Collection body = response.getBody();
//        Faculty faculty=new Faculty();
//        faculty.setStudents((List<Student>) body);
//        ResponseEntity<Faculty> facultyResponseEntity = template.postForEntity("/faculty", faculty, Faculty.class);
//        Long facultyId=facultyResponseEntity.getBody().getId();
//        response=template.getForEntity("/student/by-faculty?studentId="+facultyId, Collection.class);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isNotNull();
//        assertThat(response.getBody()).isEqualTo(body);
//
//    }не могу понять

}



