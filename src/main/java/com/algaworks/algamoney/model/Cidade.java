package com.algaworks.algamoney.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cidade")
@Getter @Setter
@EqualsAndHashCode(of = {"codigo"})
public class Cidade implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	private long codigo;
	private String nome;
	@ManyToOne
	@JoinColumn(name = "codigo_estado")
	private Estado estado;
	
}
