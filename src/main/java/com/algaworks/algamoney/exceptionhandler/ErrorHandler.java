package com.algaworks.algamoney.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ErrorHandler {

	@Getter
	private String mensagemUsuario;
	@Getter
	private String mensagemDesenvolvedor;
	
}
