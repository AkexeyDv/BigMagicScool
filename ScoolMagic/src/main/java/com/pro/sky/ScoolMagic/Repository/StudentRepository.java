package com.pro.sky.ScoolMagic.Repository;

import com.pro.sky.ScoolMagic.Models.Student;
import com.pro.sky.ScoolMagic.Models.StydentByScool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Long> {
    List<Student> findAllByAge(int age);

    List<Student> findByAgeBetween(int minAge,int maxAge);
    @Query(value = "SELECT count(id) FROM student",nativeQuery = true)
    Integer countStudent();

    @Query(value = "SELECT AVG(age) FROM student",nativeQuery = true)
    Integer avgAgeStudent();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5",nativeQuery = true)
    List<StydentByScool> getStudentBottom5();
}
