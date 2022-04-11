package com.madusame.springboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.madusame.springboot.modelo.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
	Optional<Usuario> findByEmail(String email);
}
