package com.plataforma.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plataforma.model.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {

}
