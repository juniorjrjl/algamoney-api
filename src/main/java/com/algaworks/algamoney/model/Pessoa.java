package com.algaworks.algamoney.model;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pessoa")
@NoArgsConstructor
@EqualsAndHashCode(of = {"codigo"})
public class Pessoa implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter @Setter
	private long codigo;
	
	@Getter @Setter
	@NotNull
	private String nome;
	
	@Getter @Setter
	@NotNull
	private Boolean ativo;
	
	@Getter @Setter
	@Embedded
	private Endereco endereco;
	
}
