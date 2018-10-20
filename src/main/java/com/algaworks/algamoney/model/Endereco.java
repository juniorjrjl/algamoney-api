package com.algaworks.algamoney.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Embeddable
@Getter @Setter
public class Endereco implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String logradouro;
	private String numero;
	private String complemento;
	private String bairro;
	private String cep;
	@ManyToOne
	@JoinColumn(name = "codigo_cidade")
	private Cidade cidade;
	
	
}
