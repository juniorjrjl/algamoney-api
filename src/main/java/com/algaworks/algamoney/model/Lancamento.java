package com.algaworks.algamoney.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lancamento")
@NoArgsConstructor
@EqualsAndHashCode(of = {"codigo"})
public class Lancamento implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter @Setter
	private Long codigo;
	
	@Getter @Setter
	private String descricao;
	
	@Getter @Setter
	@Column(name = "data_vencimento")
	private LocalDate dataVencimento;
	
	@Getter @Setter
	@Column(name = "data_pagamento")
	private LocalDate dataPagamento;
	
	@Getter @Setter
	private BigDecimal valor;
	
	@Getter @Setter
	private String observacao;
	
	@Enumerated(EnumType.STRING)
	@Getter @Setter
	private TipoLancamento tipo;
	
	@ManyToOne
	@JoinColumn(name = "codigo_categoria")
	private Categoria categoria;
	
	@ManyToOne
	@JoinColumn(name = "codigo_pessoa")
	private Pessoa pessoa;
	
}
