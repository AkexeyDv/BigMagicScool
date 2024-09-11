package com.pro.sky.ScoolMagic.TestControllers.TestRestControllers;

import com.pro.sky.ScoolMagic.Controllers.FacultyController;
import com.pro.sky.ScoolMagic.Controllers.StudentController;
import com.pro.sky.ScoolMagic.Services.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class StudentControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private StudentServiceImpl studentService;


    @Test
    void createStudent() {
    }

    @Test
    void editStudent() {
    }

    @Test
    void listAllStudent() {
    }

    @Test
    void getStudentInFacultet() {
    }

    @Test
    void delStudent() {
    }
}