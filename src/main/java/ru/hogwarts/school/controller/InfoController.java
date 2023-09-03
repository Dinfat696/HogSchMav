package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.LongStream;

@RestController
@RequestMapping("/properties")
public class InfoController {

    @Value("${server.port:-1}")
    private int port;

    @GetMapping("/port")
    public int getPort() {
        return port;
    }

    @GetMapping("/sum")
    public String calculateSum() {
        long startTime = System.currentTimeMillis();
        Long result = LongStream.range(1, 50_000_000)
                .parallel()
                .sum();
        long timeConsumed = System.currentTimeMillis() - startTime;
        return "time consumed = " + timeConsumed + " ms. Result = " + result;
    }

}
