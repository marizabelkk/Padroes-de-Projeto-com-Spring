package one.digitalinnovation.gof.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class ClienteRequest {

	@NotBlank(message = "O nome e obrigatorio")
	private String nome;

	@NotBlank(message = "O CEP e obrigatorio")
	@Pattern(regexp = "\\d{5}-?\\d{3}", message = "O CEP deve ter 8 digitos")
	private String cep;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

}