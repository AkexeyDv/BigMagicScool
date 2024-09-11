package com.pro.sky.ScoolMagic.Controllers;

import com.pro.sky.ScoolMagic.Models.Faculty;
import com.pro.sky.ScoolMagic.Models.Student;
import com.pro.sky.ScoolMagic.Services.FacultyServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/faculty")
@RestController
public class FacultyController {
    private final FacultyServiceImpl facultyService;

    public FacultyController(FacultyServiceImpl facultyService) {
        this.facultyService = facultyService;
    }
    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty){
        return new ResponseEntity<>(facultyService.createFaculty(faculty), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty){
        return ResponseEntity.ok(facultyService.editFaculty(faculty));
    }
    @GetMapping
    public List<Faculty> getAllFaculty(){
        return facultyService.getAllFaculty();
    }

    @GetMapping("{idStudent}")
    public Faculty getFacultetByStudent(@RequestParam("idStudent") Long idStudent){
        return facultyService.getFacultetByStudent(idStudent);
    }
    @DeleteMapping("{idFaculty}")
    public Faculty deleteFaculty(@RequestParam("idFaculty") Long idFaculty){
        return facultyService.deleteFaculty(idFaculty);
    }

}
