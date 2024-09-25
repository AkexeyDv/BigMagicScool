package com.pro.sky.ScoolMagic.Controllers;

import com.pro.sky.ScoolMagic.Models.Avatar;
import com.pro.sky.ScoolMagic.Models.Student;
import com.pro.sky.ScoolMagic.Models.StydentByScool;
import com.pro.sky.ScoolMagic.Services.AvatarServiceImpl;
import com.pro.sky.ScoolMagic.Services.StudentServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

@RequestMapping("/student")
@RestController
public class StudentController {
    private final StudentServiceImpl studentService;
    private final AvatarServiceImpl avatarService;
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    public StudentController(StudentServiceImpl studentService, AvatarServiceImpl avatarService) {
        this.studentService = studentService;
        this.avatarService = avatarService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student,
                                                 @RequestParam("facultyId") Long facultyId) {
        Student createdStudent = studentService.createStudent(student, facultyId);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        logger.info("Student id {}", student.getId());
        logger.info("Student name {}", student.getName());
        logger.info("Student age {}", student.getAge());
        if (studentService.findStudent(student) == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentService.updateStudent(student));
    }

    @GetMapping
    public Collection<Student> listAllStudent() {
        return ResponseEntity.ok(studentService.getAllStudent()).getBody();
    }

    @GetMapping("{facultyId}")
    public Collection<Student> getStudentInFacultet(@RequestParam("facultyId") Long facultyId) {
        return studentService.getStudentInFaculty(facultyId);
    }

    @DeleteMapping
    public ResponseEntity<Student> delStudent(@RequestParam("studentId") Long studentId) {
        return ResponseEntity.ok(studentService.deleteStudent(studentId));

    }

    @GetMapping(value = "avatar/preview/{id}")
    public ResponseEntity<byte[]> downloadAvatarPreview(@PathVariable Long id) {
        Avatar avatar = avatarService.findAvatar(id);
        if (avatar != null) {

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
            headers.setContentLength(avatar.getPreview().length);

            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getPreview());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping(value = "avatar/{id}")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.findAvatar(id);
        Path path = Path.of(avatar.getFilePath());
        try (
                InputStream is = Files.newInputStream(path);
                OutputStream os = response.getOutputStream()) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());

            is.transferTo(os);


        }

    }
    @GetMapping("/count")
    public Integer countStudent(){
        return studentService.countStudent();
    }
    @GetMapping("/midi-age")
    public Integer avgAgeStudent(){
        return studentService.avgAgeStudent();
    }

    @GetMapping("/Bottom5")
    public List<StydentByScool> getStudentBottom(){
        return  studentService.getStudentBottom();
    }


}
