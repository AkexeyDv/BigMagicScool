package com.pro.sky.ScoolMagic.TestControllers.MockTest;

import com.pro.sky.ScoolMagic.Controllers.FacultyController;
import com.pro.sky.ScoolMagic.Models.Faculty;
import com.pro.sky.ScoolMagic.Models.Student;
import com.pro.sky.ScoolMagic.Repository.FacultyRepository;
import com.pro.sky.ScoolMagic.Repository.StudentRepository;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = FacultyController.class)
class FacultyControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FacultyRepository facultyRepository;
    @MockBean
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentServiceImpl studentService;
    @SpyBean
    private FacultyServiceImpl facultyService;

    @InjectMocks
    private FacultyController facultyController;

    @Test
    void createFaculty() throws Exception {
        final Long id = 1l;
        final String name = "FacultyTest";
        final String color = "colorTestFaculty";
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);


        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void editFaculty() throws Exception {
        final Long id = 1l;
        final String name = "FacultyTest";
        final String color = "colorTestFaculty";
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", id);
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        facultyRepository.save(faculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(facultyObject)))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void getFaculty() throws Exception {
        final Long id = 1l;
        final String name = "FacultyTest";
        final String color = "colorTestFaculty";
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", id);
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        facultyRepository.save(faculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(facultyObject)))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void getAllFaculty() throws Exception {
        final Long id1 = 1l;
        final String name1 = "FacultyTest1";
        final String color1 = "colorTestFaculty1";
        Faculty faculty1=new Faculty();
        faculty1.setName(name1);
        faculty1.setColor(color1);

        final Long id2 = 2l;
        final String name2 = "FacultyTest2";
        final String color2 = "colorTestFaculty2";
        Faculty faculty2=new Faculty();
        faculty2.setName(name2);
        faculty2.setColor(color2);


        List<Faculty> mockFaculty = Arrays.asList(faculty1,faculty2);

        when(facultyService.getAllFaculty()).thenReturn(mockFaculty);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(name1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].color").value(color1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(name2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].color").value(color2));

    }

    @Test
    void deleteFaculty() throws Exception {
        final Long id = 1l;
        final String name = "FacultyTest";
        final String color = "colorTestFaculty";
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", id);
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);
        long facultyId = 1;
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));
        when(facultyService.deleteFaculty(any(Long.class))).thenReturn(faculty);


        mockMvc.perform(MockMvcRequestBuilders.delete("/faculty/1?idFaculty=1", facultyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void getFacultetByStudent() throws Exception {
        final Long id1 = 1l;
        final String name1 = "FacultyTest1";
        final String color1 = "colorTestFaculty1";
        Faculty faculty1=new Faculty();
        faculty1.setName(name1);
        faculty1.setColor(color1);
        Student student=new Student();
        student.setId(1l);

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));


        when(facultyService.getFacultetByStudent(any(Long.class))).thenReturn(faculty1);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/faculty?idStudent=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value(color1));
    }
}