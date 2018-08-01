package com.algaworks.algamoney.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("algamoney")
public class AlgamoneyApiProperty {

	@Getter
	private Seguranca seguranca = new Seguranca();
	
	@Getter @Setter
	public static class Seguranca{
		private String origemPermitida = "/**";
		private boolean enableHttps;
	}
	
}
