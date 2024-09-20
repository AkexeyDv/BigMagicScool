package com.pro.sky.ScoolMagic.Repository;

import com.pro.sky.ScoolMagic.Models.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Optional<Avatar> findByStudentId(Long studentId);
}