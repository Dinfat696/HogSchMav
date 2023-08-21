package ru.hogwarts.school.dto;

public class AvatarDto {
    private Long id;
    private Long studentId;
    private String studentName;

    public AvatarDto() {
    }

    public AvatarDto(Long id, Long studentId, String studentName) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
