package com.algaworks.algamoney.model;

import java.io.Serializable;

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
public class Permissao implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	@Id
	private long codigo;
	private String descricao;
	
}
