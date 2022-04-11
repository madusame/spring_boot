package com.madusame.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.madusame.springboot.modelo.Curso;

public interface CursoRepository extends JpaRepository<Curso,Long> {
	Curso findByNome(String nome);
	
}
