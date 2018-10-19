package com.algaworks.algamoney.dto;

import java.math.BigDecimal;

import com.algaworks.algamoney.model.Pessoa;
import com.algaworks.algamoney.model.TipoLancamento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class LancamentoEstatisticaPessoa {

	private TipoLancamento tipo;
	private Pessoa pessoa;
	private BigDecimal total;
	
}
