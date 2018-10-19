package com.algaworks.algamoney.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoLancamento {

	RECEITA("Receita"),
	DESPESA("Despesa");
	
	private final String descricao;
	
}
