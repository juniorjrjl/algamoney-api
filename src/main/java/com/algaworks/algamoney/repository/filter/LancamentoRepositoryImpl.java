package com.algaworks.algamoney.repository.filter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.algaworks.algamoney.dto.LancamentoEstatisticaCategoria;
import com.algaworks.algamoney.dto.LancamentoEstatisticaDia;
import com.algaworks.algamoney.dto.LancamentoEstatisticaPessoa;
import com.algaworks.algamoney.model.Categoria_;
import com.algaworks.algamoney.model.Lancamento;
import com.algaworks.algamoney.model.Lancamento_;
import com.algaworks.algamoney.model.Pessoa_;
import com.algaworks.algamoney.repository.lancamento.LancamentoRespositoryQuery;
import com.algaworks.algamoney.repository.projection.ResumoLancamento;

public class LancamentoRepositoryImpl implements LancamentoRespositoryQuery{

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);
		TypedQuery<Lancamento> query = entityManager.createQuery(criteria);
		adicionarRestricaoDePaginacao(query, pageable);
		return new PageImpl<Lancamento>(query.getResultList(), pageable, total(lancamentoFilter));
	}

	@Override
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		criteria.select(builder.construct(ResumoLancamento.class
				, root.get(Lancamento_.codigo), root.get(Lancamento_.descricao)
				, root.get(Lancamento_.dataVencimento), root.get(Lancamento_.dataPagamento)
				, root.get(Lancamento_.valor), root.get(Lancamento_.tipo)
				, root.get(Lancamento_.categoria).get(Categoria_.nome)
				, root.get(Lancamento_.pessoa).get(Pessoa_.nome)));
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);
		TypedQuery<ResumoLancamento> query = entityManager.createQuery(criteria);
		adicionarRestricaoDePaginacao(query, pageable);
		return new PageImpl<ResumoLancamento>(query.getResultList(), pageable, total(lancamentoFilter));
	}
	
	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder,
			Root<Lancamento> root) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		if(!StringUtils.isEmpty(lancamentoFilter.getDescricao())) {
			predicates.add(builder.like(builder.lower(root.get(Lancamento_.descricao)), "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
		}
		if (lancamentoFilter.getDataVencimentoDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoDe()));
		}
		if (lancamentoFilter.getDataVencimentoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoAte()));
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private void adicionarRestricaoDePaginacao(TypedQuery<?> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistroPorPagina = pageable.getPageSize();
		int primeiroregistroDaPagina = paginaAtual * totalRegistroPorPagina;
		query.setFirstResult(primeiroregistroDaPagina);
		query.setMaxResults(totalRegistroPorPagina);
	}
	
	private long total(LancamentoFilter lancamentoFilter) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);
		criteria.select(builder.count(root));
		return entityManager.createQuery(criteria).getSingleResult();
	}

	@Override
	public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate referencia) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<LancamentoEstatisticaCategoria> criteria = builder.createQuery(LancamentoEstatisticaCategoria.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		criteria.select(builder.construct(LancamentoEstatisticaCategoria.class, 
				root.get(Lancamento_.categoria),
				builder.sum(root.get(Lancamento_.valor))));
		LocalDate primeiroDia = referencia.withDayOfMonth(1);
		LocalDate ultimoDia = referencia.withDayOfMonth(referencia.lengthOfMonth());
		criteria.where(builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), primeiroDia),
				builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), ultimoDia));
		criteria.groupBy(root.get(Lancamento_.categoria));
		TypedQuery<LancamentoEstatisticaCategoria> typedQuery = entityManager.createQuery(criteria);
		return typedQuery.getResultList();
	}

	@Override
	public List<LancamentoEstatisticaDia> porDia(LocalDate referencia) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<LancamentoEstatisticaDia> criteria = builder.createQuery(LancamentoEstatisticaDia.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		criteria.select(builder.construct(LancamentoEstatisticaDia.class, 
				root.get(Lancamento_.tipo),
				root.get(Lancamento_.dataVencimento),
				builder.sum(root.get(Lancamento_.valor))));
		LocalDate primeiroDia = referencia.withDayOfMonth(1);
		LocalDate ultimoDia = referencia.withDayOfMonth(referencia.lengthOfMonth());
		criteria.where(builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), primeiroDia),
				builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), ultimoDia));
		criteria.groupBy(root.get(Lancamento_.tipo), root.get(Lancamento_.dataVencimento));
		TypedQuery<LancamentoEstatisticaDia> typedQuery = entityManager.createQuery(criteria);
		return typedQuery.getResultList();
	}

	@Override
	public List<LancamentoEstatisticaPessoa> porPessoa(LocalDate inicio, LocalDate fim) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<LancamentoEstatisticaPessoa> criteria = builder.createQuery(LancamentoEstatisticaPessoa.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		criteria.select(builder.construct(LancamentoEstatisticaPessoa.class, 
				root.get(Lancamento_.tipo),
				root.get(Lancamento_.pessoa),
				builder.sum(root.get(Lancamento_.valor))));
		criteria.where(builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), inicio),
				builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), fim));
		criteria.groupBy(root.get(Lancamento_.tipo), root.get(Lancamento_.pessoa));
		TypedQuery<LancamentoEstatisticaPessoa> typedQuery = entityManager.createQuery(criteria);
		return typedQuery.getResultList();
	}
	
}
