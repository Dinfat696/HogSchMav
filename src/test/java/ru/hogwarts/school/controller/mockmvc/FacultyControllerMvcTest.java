package ru.hogwarts.school.controller.mockmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.reposirory.FacultyRepository;
import ru.hogwarts.school.reposirory.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(FacultyController.class)
public class FacultyControllerMvcTest {
    @SpyBean
    FacultyService facultyService;
    @MockBean
    StudentRepository studentRepository;
    @MockBean
    FacultyRepository facultyRepository;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getById() throws Exception {
        Faculty faculty = new Faculty(1L, "Mat-Mat", "red");
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id").value("1"))
                .andExpect((ResultMatcher) jsonPath("$.name").value("Mat-Mat"))
                .andExpect((ResultMatcher) jsonPath("$.color").value("red"));


    }

    @Test
    void create() throws Exception {
        Faculty faculty = new Faculty(1L, "Mat-Mat", "red");
        when(facultyRepository.save(ArgumentMatchers.any(Faculty.class))).thenReturn(faculty);
        mockMvc.perform(post("/faculty")
                        .content(objectMapper.writeValueAsString(faculty))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id").value("1"))
                .andExpect((ResultMatcher) jsonPath("$.name").value("Mat-Mat"))
                .andExpect((ResultMatcher) jsonPath("$.color").value("red"));

    }

    @Test
    void update() throws Exception {
        Faculty faculty = new Faculty(1L, "Mat-Mat", "red");
        when(facultyRepository.save(ArgumentMatchers.any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        mockMvc.perform(put("/faculty/" + faculty.getId())
                        .content(objectMapper.writeValueAsString(faculty))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id").value("1"))
                .andExpect((ResultMatcher) jsonPath("$.name").value("Mat-Mat"))
                .andExpect((ResultMatcher) jsonPath("$.color").value("red"));
    }

    @Test
    void delete_faculty() throws Exception {
        Faculty faculty = new Faculty(1L, "Mat-Mat", "red");
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        mockMvc.perform(delete("/faculty/" + faculty.getId())
                        .content(objectMapper.writeValueAsString(faculty))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id").value("1"))
                .andExpect((ResultMatcher) jsonPath("$.name").value("Mat-Mat"))
                .andExpect((ResultMatcher) jsonPath("$.color").value("red"));

    }
//
//    @Test
//    void filteredBetween() throws Exception {
//        when(studentRepository.findAllByAgeBetween(0, 20)).thenReturn(Arrays.asList(
//                new Student(1l, "Ivan", 20),
//                new Student(2l, "Varina", 21)
//        ));
//        mockMvc.perform(MockMvcRequestBuilders.get("/student/age-between?min=0&max=20")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect((ResultMatcher) jsonPath("$").isArray())
//                .andExpect((ResultMatcher) jsonPath("$[0].id").value(1L))
//                .andExpect((ResultMatcher) jsonPath("$[1].id").value(2L));
//
//
//    }
//
    @Test
    void getAll() throws Exception {
        when(facultyRepository.findAll()).thenReturn(Arrays.asList(
                new Faculty(1l, "Mat-Mat", "red"),
                new Faculty(2l, "Fis-Fis", "blue")
        ));

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect((ResultMatcher) jsonPath("$").isArray())
                .andExpect((ResultMatcher) jsonPath("$[0].id").value(1L))
                .andExpect((ResultMatcher) jsonPath("$[1].id").value(2L));


    }

    @Test
    void filteredByColor() throws Exception {
        when(facultyRepository.findAllByColor("black")).thenReturn(Arrays.asList(
                new Faculty(1l, "Mat", "black"),
                new Faculty(2l, "Fis", "red"),
                new Faculty(3l, "Rus", "black"),
                new Faculty(4l, "Alg", "blue")
        ));
        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/filtered?color=black")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect((ResultMatcher) jsonPath("$").isArray())
                .andExpect((ResultMatcher) jsonPath("$[0].id").value(1L))
                .andExpect((ResultMatcher) jsonPath("$[2].id").value(3L));
    }
    @Test
    void filteredByColorOrName() throws Exception {
        when(facultyRepository.findAllByColorLikeIgnoreCaseOrNameLikeIgnoreCase("blue","Mat")).thenReturn(Arrays.asList(
                new Faculty(1l, "Mat", "black"),
                new Faculty(2l, "Fis", "red"),
                new Faculty(3l, "Rus", "black"),
                new Faculty(4l, "Alg", "blue")
        ));
        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/by-color-or-name?colorOrName=blue&colorOrName=Fis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect((ResultMatcher) jsonPath("$").isArray());


    }
}
