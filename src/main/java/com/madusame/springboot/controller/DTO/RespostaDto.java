package com.madusame.springboot.controller.DTO;

import com.madusame.springboot.modelo.Resposta;

public class RespostaDto {
	
	private Long id;
	private String nomeAutor;
	private String mensagem;
	
	public RespostaDto(Resposta resposta) {
		this.id = resposta.getId();
		this.nomeAutor = resposta.getAutor().getNome();
		this.mensagem = resposta.getMensagem();
	}

	public Long getId() {
		return id;
	}

	public String getNomeAutor() {
		return nomeAutor;
	}

	public String getMensagem() {
		return mensagem;
	}
	
	
}
