package com.pro.sky.ScoolMagic.TestControllers.MockTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pro.sky.ScoolMagic.Controllers.FacultyController;
import com.pro.sky.ScoolMagic.Controllers.StudentController;
import com.pro.sky.ScoolMagic.Models.Avatar;
import com.pro.sky.ScoolMagic.Models.Faculty;
import com.pro.sky.ScoolMagic.Models.Student;
import com.pro.sky.ScoolMagic.Repository.AvatarRepository;
import com.pro.sky.ScoolMagic.Repository.FacultyRepository;
import com.pro.sky.ScoolMagic.Repository.StudentRepository;
import com.pro.sky.ScoolMagic.Services.AvatarServiceImpl;
import com.pro.sky.ScoolMagic.Services.FacultyServiceImpl;
import com.pro.sky.ScoolMagic.Services.StudentServiceImpl;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = StudentController.class)
class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private StudentServiceImpl studentService;

    @MockBean
    private AvatarServiceImpl avatarService;
    @MockBean
    private AvatarRepository avatarRepository;


    @InjectMocks
    private StudentController studentController;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createStudent() throws Exception {
        final Long idFaculty = 1l;
        final String nameFaculty = "FacultyTest";
        final String colorFaculty = "colorTestFaculty";
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", nameFaculty);
        facultyObject.put("color", colorFaculty);

        Faculty faculty = new Faculty();
        faculty.setId(idFaculty);
        faculty.setName(nameFaculty);
        faculty.setColor(colorFaculty);

        final Long idStudent = 1l;
        final String nameStudent = "StudentTest";
        final int ageStudent = 12;

        JSONObject studentObject = new JSONObject();
        studentObject.put("id", idStudent);
        studentObject.put("name", nameStudent);
        studentObject.put("age", ageStudent);

        Student student = new Student();
        student.setId(idStudent);
        student.setName(nameStudent);
        student.setAge(ageStudent);

        //when(facultyService.getFaculty(any(Long.class))).thenReturn(faculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentService.createStudent(any(Student.class), any(Long.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student?facultyId=1")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(nameStudent))
                .andExpect(jsonPath("$.age").value(ageStudent));
    }

    @Test
    void editStudent() throws Exception {
        final Long idFaculty = 1l;
        final String nameFaculty = "FacultyTest";
        final String colorFaculty = "colorTestFaculty";
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", idFaculty);
        facultyObject.put("name", nameFaculty);
        facultyObject.put("color", colorFaculty);

        Faculty faculty = new Faculty();
        faculty.setId(idFaculty);
        faculty.setName(nameFaculty);
        faculty.setColor(colorFaculty);

        final Long idStudent = 1l;
        final String nameStudent = "StudentCreate";
        final int ageStudent = 12;


        Student student = new Student();
        student.setId(idStudent);
        student.setName(nameStudent);
        student.setAge(ageStudent);
        student.setFaculty(faculty);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        student = studentRepository.save(student);

        JSONObject studentObject = new JSONObject();
        studentObject.put("id", 1l);
        studentObject.put("name", "New Name");
        studentObject.put("age", student.getAge());
        studentObject.put("faculty", faculty);
        when(studentService.updateStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .content(objectMapper.writeValueAsString(student))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(nameStudent))
                .andExpect(jsonPath("$.age").value(ageStudent));

    }

    @Test
    void listAllStudent() throws Exception {
        final Long id1 = 1l;
        final String name1 = "StudentTest1";
        final int age1 = 14;
        Student student1 = new Student();
        student1.setName(name1);
        student1.setAge(age1);

        final Long id2 = 2l;
        final String name2 = "StudentTest2";
        final int age2 = 14;
        Student student2 = new Student();
        student2.setName(name2);
        student2.setAge(age2);


        List<Student> mockStudent = Arrays.asList(student1, student2);

        when(studentService.getAllStudent()).thenReturn(mockStudent);

        mockMvc.perform(MockMvcRequestBuilders.get("/student")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(name1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(age1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(name2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].age").value(age2));
    }

    @Test
    void getStudentInFacultet() throws Exception {
        final Long id1 = 1l;
        final String name1 = "StudentTest1";
        final int age1 = 14;
        Student student1 = new Student();
        student1.setName(name1);
        student1.setAge(age1);

        final Long id2 = 2l;
        final String name2 = "StudentTest2";
        final int age2 = 14;
        Student student2 = new Student();
        student2.setName(name2);
        student2.setAge(age2);
        List<Student> mockStudent = Arrays.asList(student1, student2);


        when(studentService.getStudentInFaculty(any(Long.class))).thenReturn(mockStudent);
        mockMvc.perform(MockMvcRequestBuilders.get("/student/1?facultyId=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(name1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(age1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(name2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].age").value(age2));
    }

    @Test
    void delStudent() throws Exception {
        final Long idStudent = 1l;
        final String nameStudent = "StudentTest";
        final int ageStudent = 12;

        JSONObject studentObject = new JSONObject();
        studentObject.put("id", idStudent);
        studentObject.put("name", nameStudent);
        studentObject.put("age", ageStudent);

        Student student = new Student();
        student.setId(idStudent);
        student.setName("TestStudent");
        student.setAge(16);

        when(studentService.deleteStudent(any(Long.class))).thenReturn(student);


        mockMvc.perform(MockMvcRequestBuilders.delete("/student?studentId=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void downloadAvatarPreview() throws Exception {
        long avatarId = 1L;
        String path="src/test/avatar.png";
        byte[] avatarData = Files.readAllBytes(Path.of(path));
        String mediaType = "image/png";
        long fileSize = avatarData.length;

        Avatar avatar = new Avatar();
        avatar.setId(avatarId);
        avatar.setFilePath(path);
        avatar.setMediaType(mediaType);
        avatar.setFileSize(fileSize);
        avatarService.findAvatar(1l);
        when(avatarService.findAvatar(avatarId)).thenReturn(avatar);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/avatar/preview/1", avatarId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType(mediaType)))
                .andExpect(content().bytes(avatarData));


    }

    @Test
    public void downloadAvatar() throws Exception {
        long avatarId = 1L;
        String path="src/test/avatar.png";
        byte[] avatarData = Files.readAllBytes(Path.of(path));
        String mediaType = "image/png";
        long fileSize = avatarData.length;

        Avatar avatar = new Avatar();
        avatar.setId(avatarId);
        avatar.setFilePath(path);
        avatar.setMediaType(mediaType);
        avatar.setFileSize(fileSize);
        when(avatarService.findAvatar(avatarId)).thenReturn(avatar);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/avatar/1", avatarId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType(mediaType)))
                .andExpect(content().bytes(avatarData));

    }
}