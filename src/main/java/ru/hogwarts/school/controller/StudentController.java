package ru.hogwarts.school.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.io.IOException;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService service;
    private final AvatarService avatarService;

    public StudentController(StudentService service, AvatarService avatarService) {
        this.service = service;
        this.avatarService = avatarService;
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return service.create(student);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @RequestBody Student student) {
        return service.update(id, student);
    }

    @DeleteMapping("/{id}")
    public Student delete(@PathVariable Long id) {
        return service.remove(id);
    }

    @GetMapping("/{id}")
    public Student getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public Collection<Student> getAll() {
        return service.getAll();
    }

    @GetMapping("/filtered")
    public Collection<Student> getAllByAge(@RequestParam int age) {
        return service.getAllByAge(age);
    }

    @GetMapping("/age-between")
    public Collection<Student> ageBetween(@RequestParam int min, @RequestParam int max) {
        return service.getByAge(min, max);
    }

    @GetMapping("/by-faculty")
    public Collection<Student> getByFaculty(Long facultyId) {
        return service.getByFacultyId(facultyId);
    }

    @PostMapping(value = "/{studentId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> save(@PathVariable Long studentId, @RequestBody MultipartFile multipartFile) {
        try {
            return ResponseEntity.ok(avatarService.save(studentId, multipartFile));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/count")
    public Long getCountStudent() {
        return service.getCountStudent();
    }

    @GetMapping("/average-age")
    public Double getAverageAgeStudents() {
        return service.getAverageAgeStudents();
    }

    @GetMapping("/last-five")
    public ResponseEntity<List<Student>> findLastFiveStudents() {
        List<Student> students = service.findLastFiveStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/stream/names-by-first-symbol")
    public List<String> getBySymbol(@RequestParam char symbol) {
        return service.getNameStartedBy(symbol);
    }

    @GetMapping("/stream/average-age-")
    public double getAverageAge() {
        return service.getAverageAge();
    }


}



