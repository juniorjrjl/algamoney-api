package com.algaworks.algamoney.dto;

import java.math.BigDecimal;

import com.algaworks.algamoney.model.Categoria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class LancamentoEstatisticaCategoria {

	private Categoria categoria;
	private BigDecimal total;
	
}
