package com.plataforma.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plataforma.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long>{
    
}