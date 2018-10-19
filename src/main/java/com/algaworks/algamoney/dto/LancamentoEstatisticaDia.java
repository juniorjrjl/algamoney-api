package com.algaworks.algamoney.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.algaworks.algamoney.model.TipoLancamento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter 
public class LancamentoEstatisticaDia {

	private TipoLancamento tipo;
	private LocalDate dia;
	private BigDecimal total;
	
}
