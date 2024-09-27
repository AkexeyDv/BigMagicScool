package com.pro.sky.ScoolMagic.Services;

import com.pro.sky.ScoolMagic.Exception.ExceptionApp;
import com.pro.sky.ScoolMagic.Models.Faculty;
import com.pro.sky.ScoolMagic.Models.Student;
import com.pro.sky.ScoolMagic.Models.StydentByScool;
import com.pro.sky.ScoolMagic.Repository.FacultyRepository;
import com.pro.sky.ScoolMagic.Repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student createStudent(Student student, Long idFaculty) {
        logger.info("Вызов метода createStudent");
        Faculty faculty = facultyRepository.findById(idFaculty)
                .orElseThrow(() -> {
                    logger.warn("Попытка поместить студента в несуществующий факультет с id " + idFaculty);
                    new ExceptionApp("Нет факультета с id " + idFaculty);
                    return null;
                });

        Student saveStudent = new Student();
        saveStudent.setName(student.getName());
        saveStudent.setAge(student.getAge());
        saveStudent.setFaculty(faculty);
        return studentRepository.save(saveStudent);
    }

    public Student updateStudent(Student student) {
        logger.info("Вызван метод updateStudent");
        Student updatedStudent = studentRepository.findById(student.getId())
                .orElseThrow(() -> {
                    logger.warn("Студент с id "+student.getId()+" несуществует");
                    new ExceptionApp("Студент с таким id не найден");
                    return null;
                });
        updatedStudent.setName(student.getName());
        updatedStudent.setAge(student.getAge());
        facultyRepository.findById(student.getFaculty().getId())
                .orElseThrow(() -> {
                    logger.error("Попытка поместить студента в несуществующий факультет с id "
                            + student.getFaculty().getId());
                    new ExceptionApp("Нет факультета с id " + student.getFaculty().getId());
                    return null;
                });
        updatedStudent.setFaculty(student.getFaculty());
        return studentRepository.save(updatedStudent);
    }

    public Student findStudent(Student student) {
        logger.info("Вызван метод findStudent");
        return studentRepository.findById(student.getId())
                .orElseThrow(()->{
                    logger.warn("Студент не обнаружен");
                    return null;
                });
    }

    public Student findStudentById(Long idStudent) {
        logger.info("Вызван метод findStudentById");
        return studentRepository.findById(idStudent).orElseThrow(()->{
            logger.warn("Неудачный поиск студента с id "+idStudent);
            return null;
        });
    }

    public List<Student> getAllStudent() {
        logger.info("Вызван метод getAllStudent");
        return studentRepository.findAll();
    }

    public List<Student> getByAgeStudent(int ageStudent) {
        logger.info("Вызван метод getByAgeStudent");
        return studentRepository.findAllByAge(ageStudent);
    }

    public List<Student> getByAgeRange(int minAge, int maxAge) {
        logger.info("Вызван метод getByAgeRange");
        return studentRepository.findByAgeBetween(minAge, maxAge);

    }

    public List<Student> getStudentInFaculty(Long facultyId) {
        logger.info("Вызван метод getStudentInFaculty");
        Faculty getFaculty = facultyRepository.findById(facultyId)
                .orElseThrow(() ->{
                    logger.error("Неверное указание факультета id "+facultyId+" для поиска студентов в нем");
                    new ExceptionApp("Факультета с указанным id не существует");
                    return null;
                });
        return getFaculty.getStudents();
    }

    public Student deleteStudent(Long idStudent) {
        logger.info("Вызван метод deleteStudent");
        Student delStudent = studentRepository.findById(idStudent)
                .orElseThrow(() -> {
                    logger.warn("Не найден студент с id "+idStudent);
                    new ExceptionApp("Несуществует студента с id " + idStudent);
                    return null;
                });
        studentRepository.deleteById(idStudent);
        if (studentRepository.findById(delStudent.getId()).isPresent()) {
            logger.warn("Неудачная попытка удаления студента с id "+idStudent
                    +". Студент существует но не удаляется.");
            throw new ExceptionApp("Что-то пошло не так. Студент с id " + idStudent + " не удален");
        }

        return delStudent;
    }

    public Integer countStudent() {
        logger.info("Вызван метод countStudent");
        return studentRepository.countStudent();
    }

    public Integer avgAgeStudent() {
        logger.info("Вызван метод avgAgeStudent");
        return studentRepository.avgAgeStudent();
    }

    public List<StydentByScool> getStudentBottom() {
        logger.info("Вызван метод getStudentBottom");
        return studentRepository.getStudentBottom5();
    }

}
