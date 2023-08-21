package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.dto.AvatarDto;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.reposirory.AvatarRepository;
import ru.hogwarts.school.reposirory.StudentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;
    @Value("${path.to.avatars.folder}")
    private Path avatarPath;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    public Avatar getById(Long id) {
        return avatarRepository.findById(id).orElseThrow();
    }

    public Long save(Long studentId, MultipartFile multipartFile) throws IOException {
        Files.createDirectories(avatarPath);
        int doIndex = multipartFile.getOriginalFilename().lastIndexOf(".");
        String fileExtension = multipartFile.getOriginalFilename().substring(doIndex + 1);
        Path path = avatarPath.resolve(studentId + "." + fileExtension);
        byte[] data = multipartFile.getBytes();
        Files.write(path, data, StandardOpenOption.CREATE);

        Student studentReference = (studentRepository.getReferenceById(studentId));
        Avatar avatar = avatarRepository.findFirstByStudent(studentReference).orElse(new Avatar());
        avatar.setStudent(studentReference);
        avatar.setMediaType(multipartFile.getContentType());
        avatar.setFileSize(multipartFile.getSize());
        avatar.setData(data);
        avatar.setFilePath(path.toAbsolutePath().toString());
        avatarRepository.save(avatar);
        return avatar.getId();


    }

    public List<AvatarDto> findAvatarsPaginated(int pageNumber) {
        return avatarRepository.findAll(PageRequest.of(pageNumber, 5))
                .getContent()
                .stream()
                .map(a -> new AvatarDto(a.getId(), a.getStudent().getId(), a.getStudent().getName()))
                .collect(Collectors.toList());


    }
}
