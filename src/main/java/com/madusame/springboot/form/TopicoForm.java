package com.madusame.springboot.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.madusame.springboot.modelo.Curso;
import com.madusame.springboot.modelo.Topico;
import com.madusame.springboot.repository.CursoRepository;

public class TopicoForm {

	@NotNull @NotEmpty @Length(min = 5,max= 20)
	private String titulo;
	
	@NotNull @NotEmpty @Length(min = 5, max= 120)
	private String mensagem;
	
	@NotNull @NotEmpty
	private String curso;
	
	public Topico converter(CursoRepository cursoRepo) {
		Curso curso = cursoRepo.findByNome(this.curso);
		return new Topico(this.titulo,this.mensagem,curso);
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}
	
	
	
}
