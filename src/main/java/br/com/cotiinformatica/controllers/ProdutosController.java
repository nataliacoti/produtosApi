package br.com.cotiinformatica.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cotiinformatica.dtos.ApiResponseDto;
import br.com.cotiinformatica.dtos.CategoriaResponseDto;
import br.com.cotiinformatica.dtos.ProdutoRequestDto;
import br.com.cotiinformatica.dtos.ProdutoResponseDto;
import br.com.cotiinformatica.entities.Produto;
import br.com.cotiinformatica.repositories.CategoriaRepository;
import br.com.cotiinformatica.repositories.ProdutoRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/produtos")
public class ProdutosController {

	private ModelMapper mapper = new ModelMapper();
	
	@Operation(summary = "Serviço para cadastro de produto.")
	@PostMapping("cadastrar")
	public ResponseEntity<ApiResponseDto> cadastrar(@RequestBody @Valid ProdutoRequestDto request) {

		var response = new ApiResponseDto();
		
		try {
			
			//verificar se a categoria não existe no banco de dados
			var categoriaRepository = new CategoriaRepository();
			
			if(!categoriaRepository.exists(request.getCategoriaId())) {
				response.setMessage("Categoria não encontrada, verifique o ID informado.");
				response.setStatusCode(400); //BAD REQUEST
			}
			else {
				
				var produto = mapper.map(request, Produto.class); //capturar os dados do produto
				produto.setId(UUID.randomUUID()); //gerando um ID para gravar o produto
				
				//gravar o produto no banco de dados
				var produtoRepository = new ProdutoRepository();
				produtoRepository.insert(produto, request.getCategoriaId());
				
				response.setMessage("Produto cadastrado com sucesso.");
				response.setId(produto.getId());
				response.setStatusCode(201);
			}	
		}
		catch(Exception e) {
			response.setMessage(e.getMessage());
			response.setStatusCode(500);
		}		
		
		return ResponseEntity.status(response.getStatusCode()).body(response);
	}

	@Operation(summary = "Serviço para atualização de dados de produto.")
	@PutMapping("atualizar/{id}")
	public ResponseEntity<ApiResponseDto> atualizar(@PathVariable UUID id, @RequestBody @Valid ProdutoRequestDto request) {

		var response = new ApiResponseDto();
		
		try {			
			var produtoRepository = new ProdutoRepository();
			if( ! produtoRepository.exists(id)) {
				response.setMessage("Produto não encontrado, verifique o ID informado.");
				response.setStatusCode(400); //BAD REQUEST
			}
			else {
				
				var categoriaRepository = new CategoriaRepository();
				if( ! categoriaRepository.exists(request.getCategoriaId())) {
					response.setMessage("Categoria não encontrada, verifique o ID informado.");
					response.setStatusCode(400); //BAD REQUEST
				}
				else {
					var produto = mapper.map(request, Produto.class);
					produto.setId(id);
					
					produtoRepository.update(produto, request.getCategoriaId());
					
					response.setMessage("Produto atualizado com sucesso.");
					response.setId(produto.getId());
					response.setStatusCode(200);
				}
			}
		}
		catch(Exception e) {
			response.setMessage(e.getMessage());
			response.setStatusCode(500);
		}	
		
		return ResponseEntity.status(response.getStatusCode()).body(response);
	}

	@Operation(summary = "Serviço para exclusão de um produto.")
	@DeleteMapping("excluir/{id}")
	public ResponseEntity<ApiResponseDto> excluir(@PathVariable UUID id) {

		var response = new ApiResponseDto();
		
		try {
			var produtoRepository = new ProdutoRepository();
			if( ! produtoRepository.exists(id)) {
				response.setMessage("Produto não encontrado, verifique o ID informado.");
				response.setStatusCode(400); //BAD REQUEST
			}
			else {
				produtoRepository.delete(id);
				
				response.setMessage("Produto excluído com sucesso.");
				response.setId(id);
				response.setStatusCode(200);
			}
		}
		catch(Exception e) {
			response.setMessage(e.getMessage());
			response.setStatusCode(500);
		}
		
		return ResponseEntity.status(response.getStatusCode()).body(response);
	}

	@Operation(summary = "Serviço para consulta de todos os produtos.")
	@GetMapping("consultar/{nome}")
	public List<ProdutoResponseDto> consultar(@PathVariable String nome) {

		try {
			
			var produtoRepository = new ProdutoRepository();
			var produtos = produtoRepository.findAll(nome);
			
			var response = new ArrayList<ProdutoResponseDto>();
			
			for(var produto : produtos) {				
				var dto = mapper.map(produto, ProdutoResponseDto.class);
				dto.setCategoria(mapper.map(produto.getCategoria(), CategoriaResponseDto.class));
				
				response.add(dto);
			}
			
			return response;			
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Operation(summary = "Serviço para consulta de 1 produto através do id.")
	@GetMapping("obter/{id}")
	public ProdutoResponseDto consultar(@PathVariable UUID id) {

		try {
			
			var produtoRepository = new ProdutoRepository();
			var produto = produtoRepository.findById(id);

			if(produto != null) {
				
				var response = mapper.map(produto, ProdutoResponseDto.class);
				response.setCategoria(mapper.map(produto.getCategoria(), CategoriaResponseDto.class));
				
				return response;
			}			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}





