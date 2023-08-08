package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public class FacultyAlreadyExistsExeption extends RuntimeException {
        public FacultyAlreadyExistsExeption() {
        }

        public FacultyAlreadyExistsExeption(String message) {
            super(message);
        }

        public FacultyAlreadyExistsExeption(String message, Throwable cause) {
            super(message, cause);
        }

        public FacultyAlreadyExistsExeption(Throwable cause) {
            super(cause);
        }

        public FacultyAlreadyExistsExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }


