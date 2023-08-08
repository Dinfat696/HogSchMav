package ru.hogwarts.school.reposirory;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Faculty;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    List<Faculty> findAllByColor(String color);

    List<Faculty> findAllByColorLikeIgnoreCaseOrNameLikeIgnoreCase(String name, String color);
}
