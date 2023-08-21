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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = HogwartsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTestRestTemplate {
    public static final Faculty PHILOSOPHY = new Faculty(null, "philosophy", "red");
    public static final Faculty PHYSICS = new Faculty(null, "mat", "blue");
    @Autowired
    TestRestTemplate template;
    @Autowired
    FacultyRepository facultyRepository;
    @Autowired
    StudentRepository studentRepository;

    @BeforeEach
    void init() {
        template.postForEntity("/faculty", PHILOSOPHY, Faculty.class);
        template.postForEntity("/faculty", PHYSICS, Faculty.class);
    }

    @Autowired
    void clearDB() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    private ResponseEntity<Faculty> createFaculty(String name, String color) {
        Faculty request = new Faculty();
        request.setName(name);
        request.setColor(color);
        ResponseEntity<Faculty> response = template.postForEntity("/faculty", request, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        return response;

    }


    @Test
    void create() {
        ResponseEntity<Faculty> response = createFaculty("math", "blue");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("math");
        assertThat(response.getBody().getColor()).isEqualTo("blue");
    }

    @Test
    void getById() {
        ResponseEntity<Faculty> faculty = createFaculty("math", "blue");
        assertThat(faculty.getBody()).isNotNull();
        Long id = faculty.getBody().getId();
        ResponseEntity<Faculty> response = template.getForEntity("/faculty/" + id, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(id);
        assertThat(response.getBody().getName()).isEqualTo("math");
        assertThat(response.getBody().getColor()).isEqualTo("blue");
    }

    @Test
    void getAll() {
        ResponseEntity<Collection> response = template.getForEntity("/faculty", Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        Collection<Faculty> body = response.getBody();
        assertThat(body.isEmpty()).isFalse();
        assertThat(body.size()).isEqualTo(2);

    }

    @Test
    void update() {
        ResponseEntity<Faculty> response = createFaculty("math", "blue");
        Faculty faculty = response.getBody();
        faculty.setColor("red");
        template.put("/faculty/" + faculty.getId(), faculty);
        response = template.getForEntity("/faculty/" + faculty.getId(), Faculty.class);
        assertThat(response.getBody().getColor()).isEqualTo("red");

    }

    @Test
    void delete() {
        ResponseEntity<Faculty> response = createFaculty("math", "blue");
        template.delete("/faculty/" + response.getBody().getId());
        response = template.getForEntity("/faculty/" + response.getBody().getId(), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void filteredByColor() {
        ResponseEntity<Collection> response = template
                .getForEntity("/faculty/filtered?color=blue", Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(1);
        assertThat(response.getBody()).isNotNull();


    }

    @Test
    void filteredByColorOrByName() {
        ResponseEntity<Collection> response = template
                .getForEntity("/faculty/by-color-or-name?colorOrName=blue", Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(1);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void getbyStudent() {
        ResponseEntity<Faculty> response = createFaculty("math", "blue");
        Student student = new Student();
        Faculty expectedFaculty = response.getBody();
        student.setFaculty(expectedFaculty);
        ResponseEntity<Student> studentResponseEntity = template.postForEntity("/student", student, Student.class);
        long studentId = studentResponseEntity.getBody().getId();
        response = template.getForEntity("/faculty/by-student?studentId=" + studentId, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(expectedFaculty);

    }

}

