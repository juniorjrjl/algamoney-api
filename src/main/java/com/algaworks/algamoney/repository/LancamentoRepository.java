package com.algaworks.algamoney.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.algamoney.model.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
