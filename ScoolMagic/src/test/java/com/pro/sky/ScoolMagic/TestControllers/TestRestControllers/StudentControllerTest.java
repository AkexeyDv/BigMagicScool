package com.pro.sky.ScoolMagic.TestControllers.TestRestControllers;

import com.pro.sky.ScoolMagic.Controllers.FacultyController;
import com.pro.sky.ScoolMagic.Controllers.StudentController;
import com.pro.sky.ScoolMagic.Models.Faculty;
import com.pro.sky.ScoolMagic.Models.Student;
import com.pro.sky.ScoolMagic.Services.StudentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Faculty testFaculty = new Faculty();
        testFaculty.setName("TestFromCreateStudent");
        testFaculty.setColor("TestColor");
        Faculty response = testRestTemplate.postForObject("http://localhost:" + port
                + "/faculty", testFaculty, Faculty.class);
        Student testStudent = new Student();
        testStudent.setName("TestFromStudentCreate");
        testStudent.setAge(16);
        testStudent.setFaculty(response);
        Assertions.assertEquals(HttpStatus.CREATED, testRestTemplate.postForEntity("http://localhost:"
                + port + "/student?facultyId=" + response.getId(), testStudent, Student.class).getStatusCode());
    }

    @Test
    void editStudent() {
        Faculty testFaculty = new Faculty();
        testFaculty.setName("TestFromEditStudent");
        testFaculty.setColor("TestColor");
        Faculty responseFaculty = testRestTemplate.postForObject("http://localhost:" + port
                + "/faculty", testFaculty, Faculty.class);
        Student testStudent = new Student();
        testStudent.setName("TestFromEditStudent");
        testStudent.setAge(16);
        testStudent.setFaculty(responseFaculty);
        Student responseStudent = testRestTemplate.postForObject("http://localhost:" + port
                + "/student?facultyId=" + responseFaculty.getId(), testStudent, Student.class);
        int newAge = 14;
        String newName = "EditingName";
        responseStudent.setAge(newAge);
        responseStudent.setName(newName);
        testRestTemplate.put("http://localhost:" + port
                + "/student", responseStudent);
        Student getStudent = studentService.findStudentById(responseStudent.getId());
        Assertions.assertEquals(newAge, getStudent.getAge());
    }

    @Test
    void listAllStudent() {
        int size1 = testRestTemplate.getForObject("http://localhost:" + port
                + "/student", List.class).size();
        Faculty testFaculty = new Faculty();
        testFaculty.setName("TestGetAllStudent");
        testFaculty.setColor("TestColor");
        Faculty responseFaculty = testRestTemplate.postForObject("http://localhost:" + port
                + "/faculty", testFaculty, Faculty.class);
        Student testStudent = new Student();
        testStudent.setName("TestFromGetAllStudent1");
        testStudent.setAge(16);
        testStudent.setFaculty(responseFaculty);
        testRestTemplate.postForObject("http://localhost:" + port
                + "/student?facultyId=" + responseFaculty.getId(), testStudent, Student.class);
        testStudent.setName("TestFromGrtAllStudent2");
        testStudent.setAge(18);
        testRestTemplate.postForObject("http://localhost:" + port
                + "/student?facultyId=" + responseFaculty.getId(), testStudent, Student.class);
        int size2 = testRestTemplate.getForObject("http://localhost:" + port
                + "/student", List.class).size();
        Assertions.assertEquals(size1 + 2, size2);

    }

    @Test
    void getStudentInFacultet() {
        Map<String, String> param = new HashMap<>();
        Faculty testFaculty = new Faculty();
        testFaculty.setName("TestFromGetStudentInFaculty");
        testFaculty.setColor("TestColor");
        Faculty responseFaculty = testRestTemplate.postForObject("http://localhost:" + port
                + "/faculty", testFaculty, Faculty.class);
        Student testStudent = new Student();
        testStudent.setName("TestFromGetStudentInFaculty");
        param.put("facultyId", responseFaculty.getId().toString());
        testStudent.setAge(16);
        testStudent.setFaculty(responseFaculty);
        testRestTemplate.postForObject("http://localhost:" + port
                + "/student?facultyId=" + responseFaculty.getId(), testStudent, Student.class);
        testRestTemplate.postForObject("http://localhost:" + port
                + "/student?facultyId=" + responseFaculty.getId(), testStudent, Student.class);
        System.out.println();
        int countAddStudent = 2;
        Assertions.assertEquals(countAddStudent, testRestTemplate.getForObject("http://localhost:"
                        + port + "/student/" + responseFaculty.getId() + "?facultyId={facultyId}"
                , List.class, param).size());
    }

    @Test
    void delStudent() {
        Map<String, String> param = new HashMap<>();
        Faculty testFaculty = new Faculty();
        testFaculty.setName("TestFromDelStudent");
        testFaculty.setColor("TestColor");
        Faculty responseFaculty = testRestTemplate.postForObject("http://localhost:" + port
                + "/faculty", testFaculty, Faculty.class);
        Student testStudent = new Student();
        testStudent.setName("TestFromDelStudent");
        param.put("facultyId", responseFaculty.getId().toString());
        testStudent.setAge(16);
        testStudent.setFaculty(responseFaculty);
        Student postStusent=testRestTemplate.postForObject("http://localhost:" + port
                + "/student?facultyId=" + responseFaculty.getId(), testStudent, Student.class);
        Assertions.assertEquals(postStusent.toString(),studentService
                .findStudent(postStusent).toString());
        testRestTemplate.delete("http://localhost:" + port
                + "/student?studentId="+postStusent.getId());
        Assertions.assertThrows(Exception.class,()->studentService
                .findStudent(postStusent).toString());


    }
}