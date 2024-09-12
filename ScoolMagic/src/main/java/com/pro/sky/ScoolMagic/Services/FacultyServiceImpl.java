package com.pro.sky.ScoolMagic.Services;

import com.pro.sky.ScoolMagic.Exception.ExceptionApp;
import com.pro.sky.ScoolMagic.Models.Faculty;
import com.pro.sky.ScoolMagic.Models.Student;
import com.pro.sky.ScoolMagic.Repository.FacultyRepository;
import com.pro.sky.ScoolMagic.Repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        Faculty saveFaculty = new Faculty();
        saveFaculty.setName(faculty.getName());
        saveFaculty.setColor(faculty.getColor());
        return facultyRepository.save(saveFaculty);
    }

    public Faculty editFaculty(Faculty faculty) {
        Faculty editingFaculty = facultyRepository.findById(faculty.getId())
                .orElseThrow(() -> new ExceptionApp("Указанный id факультета " + faculty.getId() +
                        " отсутствует"));
        editingFaculty.setName(faculty.getName());
        editingFaculty.setColor(faculty.getColor());
        return facultyRepository.save(editingFaculty);
    }

    public Faculty getFaculty(Long idFaculty){
        return facultyRepository.findById(idFaculty)
                .orElseThrow(() -> new ExceptionApp("Указанный id факультета " +
                        idFaculty +" отсутствует"));
    }

    public List<Faculty> getAllFaculty(){
        return facultyRepository.findAll();
    }

    public Faculty getFacultetByStudent(Long idStudent){
        Student findStudent=studentRepository.findById(idStudent)
                .orElseThrow(()->new ExceptionApp("Студента с id "+idStudent+" нет"));
        return findStudent.getFaculty();
    }

    public Faculty deleteFaculty(Long idFaculty){
        Faculty delFaculty=facultyRepository.findById(idFaculty)
                .orElseThrow(()->new ExceptionApp("Факультета с id "+idFaculty+" нет"));
        if(!delFaculty.getStudents().isEmpty()){
            throw new ExceptionApp("В факультете с id " + idFaculty+" есть студенты. Удаление невозможно");
        }
        facultyRepository.deleteById(idFaculty);
        return delFaculty;
    }
}
