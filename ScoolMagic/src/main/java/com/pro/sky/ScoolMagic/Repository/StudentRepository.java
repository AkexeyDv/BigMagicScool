package com.pro.sky.ScoolMagic.Repository;

import com.pro.sky.ScoolMagic.Models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Long> {
    List<Student> findAllByAge(int age);

    List<Student> findByAgeBetween(int minAge,int maxAge);


}
