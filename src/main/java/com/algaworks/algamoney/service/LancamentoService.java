package com.algaworks.algamoney.service;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.algaworks.algamoney.dto.LancamentoEstatisticaPessoa;
import com.algaworks.algamoney.mail.Mailer;
import com.algaworks.algamoney.model.Lancamento;
import com.algaworks.algamoney.model.Pessoa;
import com.algaworks.algamoney.model.Usuario;
import com.algaworks.algamoney.repository.LancamentoRepository;
import com.algaworks.algamoney.repository.PessoaRepository;
import com.algaworks.algamoney.repository.UsuarioRepository;
import com.algaworks.algamoney.service.exception.PessoaInexistenteOuInativoException;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@Slf4j
public class LancamentoService {

	private final static String DESTINATARIOS = "ROLE_PESQUISAR_LANCAMENTO";
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository; 
	
	@Autowired
	private Mailer mailer;
	
	/*@Autowired
	private S3 s3;*/
	
	@Scheduled(cron = "0 0 6 * * *")
	public void avisarLancamentosVencidos() {
		if (log.isDebugEnabled()) {
			log.debug("Preparando envio de emails de aviso de lançamento de avisos.");
		}
		List<Lancamento> vencidos = lancamentoRepository.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());
		if (vencidos.isEmpty()) {
			log.info("sem lançamentos vencidos para aviso.");
			return;
		}
		log.info("existem {} lançamentos vencidos.", vencidos.size());
		List<Usuario> destinatarios = usuarioRepository.findByPermissoesDescricao(DESTINATARIOS);
		if (destinatarios.isEmpty()) {
			log.warn("Existem lançamentos vencidos mas o sistema não encontrou destinatários.");
			return;
		}
		mailer.avisarSobreLancamentosVencidos(vencidos, destinatarios);
		log.info("Envio de e-mail de aviso concluído.");
	}
	
	public Lancamento salvar(Lancamento lancamento) {
		Pessoa pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		if ((pessoa == null) || (pessoa.isInativo())) {
			throw new PessoaInexistenteOuInativoException();
		}
		if (StringUtils.hasText(lancamento.getAnexo())) {
			//s3.salvar(lancamento.getAnexo());
		}
		return lancamentoRepository.save(lancamento);
	}

	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		Lancamento lancamentoSalvo = buscarLancamentoExistente(codigo);
		if (!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
			validarPessoa(lancamento);
		}
		if (StringUtils.isEmpty(lancamento.getAnexo()) && (StringUtils.hasText(lancamento.getAnexo()))) {
			//s3.remover(lancamento.getAnexo());
		}else if ((StringUtils.hasText(lancamento.getAnexo()) && (lancamento.getAnexo().equals(lancamentoSalvo.getAnexo())))){
			//s3.substituir(lancamentoSalvo.getAnexo(), lancamento.getAnexo());
		}
 		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");
 		return lancamentoRepository.save(lancamentoSalvo);
	}
	
	private void validarPessoa(Lancamento lancamento) {
		Pessoa pessoa = null;
		pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
 		if (pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativoException();
		}
	}
	
	private Lancamento buscarLancamentoExistente(Long codigo) {
		Lancamento lancamentoSalvo = lancamentoRepository.findOne(codigo);
		if (lancamentoSalvo == null) {
			throw new IllegalArgumentException();
		}
		return lancamentoSalvo;
	}
	
	public byte[] relatorioPorPessoa(LocalDate inicio, LocalDate fim) throws JRException {
		List<LancamentoEstatisticaPessoa> dados = lancamentoRepository.porPessoa(inicio, fim);
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("DT_INICIO", Date.valueOf(inicio));
		parametros.put("DT_FIM", Date.valueOf(fim));
		parametros.put("RERTO_LOCALE", new Locale("pt", "BR"));
		InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/lancamentos-por-pessoa.jasper");
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, new JRBeanCollectionDataSource(dados));
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}
	
}
