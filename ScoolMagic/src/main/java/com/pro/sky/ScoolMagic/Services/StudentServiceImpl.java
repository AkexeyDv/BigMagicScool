package com.pro.sky.ScoolMagic.Services;

import com.pro.sky.ScoolMagic.Exception.ExceptionApp;
import com.pro.sky.ScoolMagic.Models.Faculty;
import com.pro.sky.ScoolMagic.Models.Student;
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
        Faculty faculty = facultyRepository.findById(idFaculty)
                .orElseThrow(() -> new ExceptionApp("Not find faculti with id " + idFaculty));
        Student saveStudent = new Student();
        saveStudent.setName(student.getName());
        saveStudent.setAge(student.getAge());
        saveStudent.setFaculty(faculty);
        logger.info("Создание студента c id {} - не имеет значение, задается автоматически при сохранении"
                , saveStudent.getId());
        logger.info("Создание студента c name {}", saveStudent.getName());
        logger.info("Помещаем студента в факультет с  id {}", faculty.getId());
        logger.info("Название факультета {}", faculty.getName());
        return studentRepository.save(saveStudent);
    }

    public Student updateStudent(Student student) {
        logger.info("Чтение студента c id {} ", student.getId());
        Student updatedStudent=studentRepository.findById(student.getId())
                .orElseThrow(()->new ExceptionApp("Студент с таким id не найден"));
        logger.info("Изменение студента c name {}", student.getName());
        updatedStudent.setName(student.getName());
        logger.info("Изменение студента c age {}", student.getAge());
        updatedStudent.setAge(student.getAge());
        logger.info("Помещаем студента в факультет с  id {}", student.getFaculty().getId());
        logger.info("Название факультета {}", student.getFaculty().getName());
        facultyRepository.findById(student.getFaculty().getId()).orElseThrow(() ->
                new ExceptionApp("Нет факультета с id " + student.getFaculty().getId()));
        updatedStudent.setFaculty(student.getFaculty());
        return studentRepository.save(updatedStudent);
    }

    public Student findStudent(Student student){
        return studentRepository.findById(student.getId()).orElseThrow(()->new ExceptionApp("Не найти студента с таким id"));
    }

    public List<Student> getAllStudent() {
        return studentRepository.findAll();
    }

    public List<Student> getByAgeStudent(int ageStudent) {
        return studentRepository.findAllByAge(ageStudent);
    }

    public List<Student> getByAgeRange(int minAge, int maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);

    }

    public List<Student> getStudentInFaculty(Long facultyId){
        Faculty getFaculty=facultyRepository.findById(facultyId)
                .orElseThrow(()->new ExceptionApp("Факультета с указанным id не существует"));
        return getFaculty.getStudents();
    }

    public Student deleteStudent(Long idStudent) {
        Student delStudent = studentRepository.findById(idStudent)
                .orElseThrow(() -> new ExceptionApp("Not find student with id " + idStudent + " for delete"));
        studentRepository.deleteById(idStudent);
        if(studentRepository.findById(delStudent.getId()).isPresent()){
            throw new ExceptionApp("Что-то пошло не так. Студент с id " + idStudent + " не удален");
        }

        return delStudent;
    }
}
