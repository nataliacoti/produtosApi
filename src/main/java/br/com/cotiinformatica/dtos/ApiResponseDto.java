package br.com.cotiinformatica.dtos;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class ApiResponseDto {

	private Integer statusCode;
	private String message;
	private Instant timestamp = Instant.now();
	private UUID id;
}
