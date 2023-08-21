package ru.hogwarts.school.controller.mockmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.reposirory.AvatarRepository;
import ru.hogwarts.school.reposirory.FacultyRepository;
import ru.hogwarts.school.reposirory.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerMvcTest {
    @SpyBean
    StudentService studentService;
    @MockBean
    AvatarService avatarService;
    @MockBean
    StudentRepository studentRepository;
    @MockBean
    FacultyRepository facultyRepository;
    @MockBean
    AvatarRepository avatarRepository;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getById() throws Exception {
        Student student = new Student(1L, "Ivan", 20);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        mockMvc.perform(MockMvcRequestBuilders.get("/student/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id").value("1"))
                .andExpect((ResultMatcher) jsonPath("$.name").value("Ivan"))
                .andExpect((ResultMatcher) jsonPath("$.age").value("20"));


    }

    @Test
    void create() throws Exception {
        Student student = new Student(1L, "Ivan", 20);
        when(studentRepository.save(ArgumentMatchers.any(Student.class))).thenReturn(student);
        mockMvc.perform(post("/student")
                        .content(objectMapper.writeValueAsString(student))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id").value("1"))
                .andExpect((ResultMatcher) jsonPath("$.name").value("Ivan"))
                .andExpect((ResultMatcher) jsonPath("$.age").value("20"));

    }

    @Test
    void update() throws Exception {
        Student student = new Student(1L, "Ivan", 20);
        when(studentRepository.save(ArgumentMatchers.any(Student.class))).thenReturn(student);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        mockMvc.perform(put("/student/" + student.getId())
                        .content(objectMapper.writeValueAsString(student))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id").value("1"))
                .andExpect((ResultMatcher) jsonPath("$.name").value("Ivan"))
                .andExpect((ResultMatcher) jsonPath("$.age").value("20"));
    }

    @Test
    void delete_student() throws Exception {
        Student student = new Student(1L, "Ivan", 20);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        mockMvc.perform(delete("/student/" + student.getId())
                        .content(objectMapper.writeValueAsString(student))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id").value("1"))
                .andExpect((ResultMatcher) jsonPath("$.name").value("Ivan"))
                .andExpect((ResultMatcher) jsonPath("$.age").value("20"));

    }

    @Test
    void filteredBetween() throws Exception {
        when(studentRepository.findAllByAgeBetween(0, 20)).thenReturn(Arrays.asList(
                new Student(1l, "Ivan", 20),
                new Student(2l, "Varina", 21)
        ));
        mockMvc.perform(MockMvcRequestBuilders.get("/student/age-between?min=0&max=20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect((ResultMatcher) jsonPath("$").isArray())
                .andExpect((ResultMatcher) jsonPath("$[0].id").value(1L))
                .andExpect((ResultMatcher) jsonPath("$[1].id").value(2L));


    }

    @Test
    void getAll() throws Exception {
        when(studentRepository.findAll()).thenReturn(Arrays.asList(
                new Student(1l, "Ivan", 20),
                new Student(2l, "Varina", 21)
        ));

        mockMvc.perform(MockMvcRequestBuilders.get("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect((ResultMatcher) jsonPath("$").isArray())
                .andExpect((ResultMatcher) jsonPath("$[0].id").value(1L))
                .andExpect((ResultMatcher) jsonPath("$[1].id").value(2L));


    }

    @Test
    void filteredByAge() throws Exception {
        when(studentRepository.findAllByAge(22)).thenReturn(Arrays.asList(
                new Student(1l, "Ivan", 22),
                new Student(2l, "Varina", 21),
                new Student(3l, "Marina", 22),
                new Student(4l, "Slava", 21)
        ));
        mockMvc.perform(MockMvcRequestBuilders.get("/student/filtered?age=22")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect((ResultMatcher) jsonPath("$").isArray())
                .andExpect((ResultMatcher) jsonPath("$[0].id").value(1L))
                .andExpect((ResultMatcher) jsonPath("$[2].id").value(3L));
    }

    @Test
    void getByFaculty() throws Exception {
        List<Student> facultyStudents = Arrays.asList(
                new Student(1l, "Olga", 22),
                new Student(2l, "Vova", 23)
        );
        Faculty faculty = new Faculty(1L, "FIZ", "BLUE");
        faculty.setStudents(facultyStudents);
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/by-faculty?facultyId=" + faculty.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Olga"));

    }

    @Test
    void getCountOfStudentsTest() throws Exception {
        List<Student> students = Arrays.asList(
                new Student(1L, "Alice", 22),
                new Student(2L, "Bob", 24),
                new Student(3L, "Charlie", 20),
                new Student(4L, "David", 21),
                new Student(5L, "Eve", 23)
        );

        when(studentService.getCountStudent()).thenReturn((long) students.size());
        mockMvc.perform(get("/student/count")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(students.size()));
    }

    @Test
    void getAverageAgeOfStudentsTest() throws Exception {
        List<Student> students = Arrays.asList(
                new Student(1L, "Alice", 22),
                new Student(2L, "Bob", 24),
                new Student(3L, "Charlie", 20)
        );

        Double averageAge = students.stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);

        when(studentService.getAverageAgeStudents()).thenReturn(averageAge);
        mockMvc.perform(get("/student/average-age")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(averageAge));
    }

    @Test
    void findLastFiveStudentsTest() throws Exception {
        List<Student> students = Arrays.asList(
                new Student(1L, "Alice", 22),
                new Student(2L, "Bob", 24),
                new Student(3L, "Charlie", 20),
                new Student(4L, "David", 21),
                new Student(5L, "Eve", 23),
                new Student(6L, "Frank", 25),
                new Student(7L, "Grace", 19),
                new Student(8L, "Helen", 27),
                new Student(9L, "Ivan", 26),
                new Student(10L, "John", 18)
        );
        PageRequest pageable = PageRequest.of(0, 5);
        Page<Student> studentPage = new PageImpl<>(students.subList(5, 10), pageable, (long) students.size());

        when(studentRepository.findLastFiveStudents(any())).thenReturn(studentPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/last-five")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id").value(6))
                .andExpect(jsonPath("$[1].id").value(7))
                .andExpect(jsonPath("$[2].id").value(8))
                .andExpect(jsonPath("$[3].id").value(9))
                .andExpect(jsonPath("$[4].id").value(10));
    }

}






