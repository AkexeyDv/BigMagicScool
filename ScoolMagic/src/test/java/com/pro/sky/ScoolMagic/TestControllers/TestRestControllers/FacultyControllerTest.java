package com.pro.sky.ScoolMagic.TestControllers.TestRestControllers;

import com.pro.sky.ScoolMagic.Controllers.FacultyController;
import com.pro.sky.ScoolMagic.Exception.ExceptionApp;
import com.pro.sky.ScoolMagic.Models.Faculty;

import com.pro.sky.ScoolMagic.Models.Student;
import com.pro.sky.ScoolMagic.Services.FacultyServiceImpl;
import com.pro.sky.ScoolMagic.Services.StudentServiceImpl;
import jakarta.persistence.Entity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.internal.junit.ExceptionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ExceptionCollector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class FacultyControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private StudentServiceImpl studentService;

    @Test
    void createFaculty() throws Exception {
        Faculty testFaculty = new Faculty();
        testFaculty.setName("TestFromCreateFaculty");
        testFaculty.setColor("TestColorFromCreateFaculty");
        Faculty response = testRestTemplate.postForObject("http://localhost:" + port + "/faculty", testFaculty, Faculty.class);
        Assertions.assertEquals(testFaculty.getName(), response.getName());
        Assertions.assertEquals(testFaculty.getColor(), response.getColor());
        Assertions.assertNotNull(response.getId());
    }

    @Test
    void editFaculty() throws Exception{
        Faculty testFaculty = new Faculty();
        testFaculty.setName("TestFromCreateFaculty");
        testFaculty.setColor("TestColorFromCreateFaculty");
        Faculty response = testRestTemplate.postForObject("http://localhost:" + port + "/faculty", testFaculty, Faculty.class);
        response.setColor("EditingColor");
        response.setName("EditingName");
        testRestTemplate.put("http://localhost:" + port + "/faculty", response);
        Faculty editingFaculty = facultyController.getFaculty(response.getId()).getBody();
        Assertions.assertEquals(response.getColor(), editingFaculty.getColor());
        Assertions.assertEquals(response.getName(), editingFaculty.getName());
    }

    @Test
    void getAllFaculty() throws Exception{
        Assertions.assertNotEquals(0,
                testRestTemplate.getForObject("http://localhost:" + port + "/faculty", List.class).size());
    }

    @Test
    void getFacultetByStudent() {
        Faculty testFaculty = new Faculty();
        testFaculty.setName("TestFromGetFacyltyByStudent");
        testFaculty.setColor("TestColorFromGetFacyltyByStudent");
        Faculty responseFaculty = testRestTemplate.postForObject("http://localhost:" + port + "/faculty", testFaculty, Faculty.class);

        Student student = new Student();
        student.setName("TestGetFacultetByStudent");
        student.setAge(12);
        Student saveStudent = studentService.createStudent(student, responseFaculty.getId());
        Long idStudent = saveStudent.getId();
        Assertions.assertEquals(responseFaculty.getId(), saveStudent.getFaculty().getId());
        Assertions.assertEquals(saveStudent.getFaculty().getId(), testRestTemplate.getForEntity("http://localhost:" +
                        port + "/faculty/faculty?idStudent=" + idStudent, Faculty.class)
                .getBody().getId());
    }

    @Test
    void deleteFaculty() throws Exception{
        Map<String, String> param = new HashMap<>();
        Faculty testFaculty = new Faculty();
        testFaculty.setName("TestFromDeleteFaculty");
        testFaculty.setColor("TestColor");
        Faculty responseFaculty = testRestTemplate.postForObject("http://localhost:" + port + "/faculty", testFaculty, Faculty.class);
        Long idFaculty = responseFaculty.getId();
        param.put("idFindFaculty", idFaculty.toString());
        testRestTemplate.delete("http://localhost:" + port + "/faculty/{idFaculty}?idFaculty="
                + responseFaculty.getId(), Faculty.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, testRestTemplate.getForEntity("http://localhost:" + port + "/faculty/{idFindFaculty}"
                + responseFaculty.getId(), String.class, param).getStatusCode());


    }
}