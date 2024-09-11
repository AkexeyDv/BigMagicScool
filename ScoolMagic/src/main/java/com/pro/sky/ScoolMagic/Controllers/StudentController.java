package com.pro.sky.ScoolMagic.Controllers;

import com.pro.sky.ScoolMagic.Models.Student;
import com.pro.sky.ScoolMagic.Services.StudentServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RequestMapping("/student")
@RestController
public class StudentController {
    private final StudentServiceImpl studentService;
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    public StudentController(StudentServiceImpl studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student,
                                                 @RequestParam("facultyId") Long facultyId) {
        Student createdStudent=studentService.createStudent(student,facultyId);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<Student> editStudent(@RequestBody Student student){
        logger.info("Student id {}", student.getId());
        logger.info("Student name {}", student.getName());
        logger.info("Student age {}", student.getAge());
        if (studentService.findStudent(student) == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentService.updateStudent(student));
    }
    @GetMapping
    public Collection<Student> listAllStudent(){
        return ResponseEntity.ok(studentService.getAllStudent()).getBody();
    }

    @GetMapping("{facultyId}")
    public Collection<Student> getStudentInFacultet(@RequestParam("facultyId") Long facultyId){
        return studentService.getStudentInFaculty(facultyId);
    }

    @DeleteMapping
    public ResponseEntity<Student> delStudent(@RequestParam("studentId") Long studentId){
        return ResponseEntity.ok(studentService.deleteStudent(studentId));

    }

}
