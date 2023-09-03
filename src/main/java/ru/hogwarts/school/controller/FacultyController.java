package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;


@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService service;

    public FacultyController(FacultyService service) {
        this.service = service;
    }

    @PostMapping
    public Faculty create(@RequestBody Faculty faculty) {
        return service.create(faculty);
    }

    @PutMapping("/{id}")
    public Faculty update(@PathVariable Long id, @RequestBody Faculty faculty) {
        return service.update(id, faculty);
    }

    @DeleteMapping("/{id}")
    public Faculty delete(@PathVariable Long id) {
        return service.remove(id);
    }

    @GetMapping("/{id}")
    public Faculty getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public Collection<Faculty> getAll() {
        return service.getAll();
    }

    @GetMapping("/filtered")
    public Collection<Faculty> getAllByColor(@RequestParam("color") String color) {
        return service.getAllByColor(color);
    }

    @GetMapping("/by-color-or-name")
    public Collection<Faculty> filteredByColorOrName(@RequestParam String colorOrName) {
        return service.getAllByNameOrColor(colorOrName, colorOrName);
    }

    @GetMapping("/by-student")
    public Faculty getByStudent(@RequestParam Long studentId) {
        return service.getByStudentId(studentId);
    }


}


