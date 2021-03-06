package com.siertech.stapi.opcao;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonView;
import com.siertech.stapi.util.Views;

@Entity
public class Opcao {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Public.class)
	private long id;

	public Opcao(){


	}


	public Opcao(String descricao){

		this.descricao = descricao;

	}



	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}




	@JsonView(Views.Public.class)
	private String descricao;

	@JsonView(Views.Public.class)
	private String valor;

	public String getValor() {
		return valor;
	}


	public void setValor(String valor) {
		this.valor = valor;
	}





}
