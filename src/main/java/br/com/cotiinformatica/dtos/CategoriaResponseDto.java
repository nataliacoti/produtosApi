package br.com.cotiinformatica.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class CategoriaResponseDto {

	private UUID id;
	private String nome;
}
