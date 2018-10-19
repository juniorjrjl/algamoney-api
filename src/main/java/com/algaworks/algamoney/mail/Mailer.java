package com.algaworks.algamoney.mail;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.algaworks.algamoney.model.Lancamento;
import com.algaworks.algamoney.model.Usuario;

@Component
public class Mailer {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private TemplateEngine thymeleaf;
	
	/*@Autowired
	private LancamentoRepository lancamentoRepository;*/
	
	/*@EventListener
	public void teste(ApplicationReadyEvent event) {
		this.enviarEmail("testes.algaworks@gmail.com", Arrays.asList("junior.jr.jl@gmail.com"), "testando", "teste<br/> testando");
		System.out.println("Enviado.");
	}*/
	
	/*@EventListener
	public void teste(ApplicationReadyEvent event) {
		String template = "mail/aviso-lancamentos-vencidos";
		List<Lancamento> lancamentos = lancamentoRepository.findAll();
		Map<String, Object> variaveis = new HashMap<String, Object>();
		variaveis.put("lancamentos", lancamentos);
		this.enviarEmail("testes.algaworks@gmail.com", Arrays.asList("junior.jr.jl@gmail.com"), "testando", template, variaveis);
		System.out.println("Enviado.");
	}*/
	
	public void avisarSobreLancamentosVencidos(List<Lancamento> vencidos, List<Usuario> destinatarios) {
		Map<String, Object> variaveis = new HashMap<String, Object>();
		variaveis.put("lancamentos", vencidos);
		List<String> emails = destinatarios.stream().map(u -> u.getEmail()).collect(Collectors.toList());
		this.enviarEmail("junior.jr.jl@gmail.com", emails, "Lançamentos vencidos", "mail/aviso-lancamentos-vencidos", variaveis);
	}
	
	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String template, Map<String, Object> variaveis) {
		Context context = new Context(new Locale("pt", "BR"));
		variaveis.entrySet().forEach(e -> context.setVariable(e.getKey(), e.getValue()));
		String mensagem = thymeleaf.process(template, context);
		enviarEmail(remetente, destinatarios, assunto, mensagem);
	}
	
	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String mensagem) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
		try {
			helper.setFrom(remetente);
			helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
			helper.setSubject(assunto);
			helper.setText(mensagem, true);
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new RuntimeException("Problemas com o envio de e-mail", e);
		}
	}
	
}
