package com.algaworks.algamoney.repository.lancamento;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.algaworks.algamoney.dto.LancamentoEstatisticaCategoria;
import com.algaworks.algamoney.dto.LancamentoEstatisticaDia;
import com.algaworks.algamoney.dto.LancamentoEstatisticaPessoa;
import com.algaworks.algamoney.model.Lancamento;
import com.algaworks.algamoney.repository.filter.LancamentoFilter;
import com.algaworks.algamoney.repository.projection.ResumoLancamento;

public interface LancamentoRespositoryQuery{

	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);
	public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate referencia);
	public List<LancamentoEstatisticaDia> porDia(LocalDate referencia);
	public List<LancamentoEstatisticaPessoa> porPessoa(LocalDate inicio, LocalDate fim);
}
