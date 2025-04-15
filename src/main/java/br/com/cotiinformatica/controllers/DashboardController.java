package br.com.cotiinformatica.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cotiinformatica.dtos.CategoriaQtdProdutosResponseDto;
import br.com.cotiinformatica.repositories.CategoriaRepository;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

	@Operation(summary = "Serviço para consultar o total da quantidade de produtos por categoria.")
	@GetMapping("quantidade-produtos-categoria")
	public List<CategoriaQtdProdutosResponseDto> consultarQuantidadeProdutos() {
		
		try {
			var categoriaRepository = new CategoriaRepository();
			return categoriaRepository.groupByQuantidade();	
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
