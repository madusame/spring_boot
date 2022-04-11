package com.madusame.springboot.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.madusame.springboot.modelo.Topico;
import com.madusame.springboot.repository.TopicoRepository;

public class AtualizarTopicoForm {
	@NotNull @NotEmpty @Length(min = 5,max= 20)
	private String titulo;
	
	@NotNull @NotEmpty @Length(min = 5, max= 120)
	private String mensagem;

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

	public Topico atualizar(Long id, TopicoRepository topicoRepo) {
		Topico topico = topicoRepo.findById(id).get();
		topico.setMensagem(this.mensagem);
		topico.setTitulo(this.titulo);
		return topico;
	}
	
	
}
