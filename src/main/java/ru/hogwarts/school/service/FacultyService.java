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
public class FacultyService {
    @Autowired
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty create(Faculty faculty) {
        return facultyRepository.save(faculty);

    }

    public Faculty update(Long id, Faculty faculty) {
        Faculty existingFaculty = facultyRepository.findById(id).orElseThrow(FacultyNotFoundException::new);
        existingFaculty.setColor(faculty.getColor());
        existingFaculty.setName(faculty.getName());
        return facultyRepository.save(existingFaculty);


    }


    public Faculty getById(Long id) {
        return facultyRepository.findById(id).orElseThrow(FacultyNotFoundException::new);

    }

    public Collection<Faculty> getAll() {

        return facultyRepository.findAll();
    }

    public Faculty remove(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(FacultyNotFoundException::new);
        facultyRepository.delete(faculty);
        return faculty;
    }


    public Collection<Faculty> getAllByColor(String color) {
        return facultyRepository.findAllByColor(color);
    }

    public Collection<Faculty> getAllByNameOrColor(String color, String name) {
        return facultyRepository.findAllByColorLikeIgnoreCaseOrNameLikeIgnoreCase(color, name);
    }

    public Faculty getByStudentId(Long studentId) {
        return studentRepository.findById(studentId)
                .map(Student::getFaculty)
                .orElseThrow(StudentNotFoundException::new);
    }
}
