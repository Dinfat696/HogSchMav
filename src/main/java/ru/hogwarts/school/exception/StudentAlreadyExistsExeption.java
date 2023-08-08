package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StudentAlreadyExistsExeption extends RuntimeException {
    public StudentAlreadyExistsExeption() {
    }

    public StudentAlreadyExistsExeption(String message) {
        super(message);
    }

    public StudentAlreadyExistsExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public StudentAlreadyExistsExeption(Throwable cause) {
        super(cause);
    }

    public StudentAlreadyExistsExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
