package com.algaworks.algamoney.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "permissao")
@Getter @Setter
@EqualsAndHashCode(of = {"codigo"})
public class Permissao {

	@Id
	private long codigo;
	private String descricao;
	
}
