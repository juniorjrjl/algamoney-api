package com.algaworks.algamoney.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.algamoney.model.Cidade;

public interface CidadeRepository extends JpaRepository<Cidade, Long>{

	public List<Cidade> findByEstadoCodigo(Long codigoEstado);
	
}
