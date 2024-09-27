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
    private static final Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Вызван метод createFaculty");
        Faculty saveFaculty = new Faculty();
        saveFaculty.setName(faculty.getName());
        saveFaculty.setColor(faculty.getColor());

        return facultyRepository.save(saveFaculty);
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Вызван метод editFaculty");

        Faculty editingFaculty = facultyRepository.findById(faculty.getId())
                .orElseThrow(
                        () -> {
                            logger.error("Факультет с id " + faculty.getId() + " отсутствует");
                            new ExceptionApp("Указанный факультет "
                                    + faculty.getId() + " отсутствует");
                            return null;
                        });

        editingFaculty.setName(faculty.getName());
        editingFaculty.setColor(faculty.getColor());
        return facultyRepository.save(editingFaculty);
    }

    public Faculty getFaculty(Long idFaculty) {
        logger.info("Вызван метод getFaculty");
        return facultyRepository.findById(idFaculty)
                .orElseThrow(() -> {
                    logger.error("Факультет с id " + idFaculty + " отсутствует");
                    new ExceptionApp("Указанный факультет с id " + idFaculty + " отсутствует");
                    return null;
                });
    }

    public List<Faculty> getAllFaculty() {
        logger.info("Вызван метод getAllFaculty");
        return facultyRepository.findAll();
    }

    public Faculty getFacultetByStudent(Long idStudent) {
        logger.info("Вызван метод getFacultetByStudent");
        Student findStudent = studentRepository.findById(idStudent)
                .orElseThrow(() -> {
                    logger.error("Студент с id " + idStudent + " отсутствует");
                    new ExceptionApp("Студента с id " + idStudent + " нет");
                    return null;
                });
        return findStudent.getFaculty();
    }

    public Faculty deleteFaculty(Long idFaculty) {
        logger.info("Вызван метод deleteFaculty ");
        Faculty delFaculty = facultyRepository.findById(idFaculty)
                .orElseThrow(() -> {
                    logger.error("Факультет с id " + idFaculty + " отсутствует");
                    new ExceptionApp("Указанный факультет "
                            + idFaculty + " отсутствует");
                    return null;
                });

        if (!delFaculty.getStudents().isEmpty()) {
            logger.error("Нарушение ссылочной целостности - в факультете с id" + idFaculty
                    + "есть студенты");
            throw new ExceptionApp("В факультете с id " + idFaculty + " есть студенты. Удаление невозможно");
        }
        facultyRepository.deleteById(idFaculty);
        return delFaculty;
    }
}
