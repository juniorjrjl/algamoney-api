package com.algaworks.algamoney.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@ConfigurationProperties("algamoney")
public class AlgamoneyApiProperty {

	@Getter
	private final Seguranca seguranca = new Seguranca();
	@Getter
	private final Mail mail = new Mail();
	@Getter
	private final S3 s3 = new S3();
	
	@Getter @Setter
	public static class S3{
		private String accessKeyId;
		private String secretAccessKeyId;
		private String bucket = "amapi-algamoney-api";
	}
	
	@Getter @Setter
	public static class Seguranca{
		private String origemPermitida = "/**";
		private boolean enableHttps;
		private List<String> redirectsPermitidos;
		private String authServerUrl;

    }
	
	@Getter @Setter
	public static class Mail{
		private String host;
		private int port;
		
		private String username;
		private String password;
	}
	
}
