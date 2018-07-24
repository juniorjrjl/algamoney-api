package com.algaworks.algamoney.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Embeddable
public class Endereco implements Serializable{

	private static final long serialVersionUID = 1L;

	@Getter @Setter
	private String logradouro;
	
	@Getter @Setter
	private String numero;
	
	@Getter @Setter
	private String complemento;
	
	@Getter @Setter
	private String bairro;
	
	@Getter @Setter
	private String cep;
	
	@Getter @Setter
	private String cidade;
	
	@Getter @Setter
	private String estado;
	
}
