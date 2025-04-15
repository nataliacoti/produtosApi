package br.com.cotiinformatica.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.cotiinformatica.dtos.CategoriaQtdProdutosResponseDto;
import br.com.cotiinformatica.entities.Categoria;
import br.com.cotiinformatica.factories.ConnectionFactory;

public class CategoriaRepository {

	//método para consultar todas as categorias do banco de dados
	public List<Categoria> findAll() throws Exception {
		
		//abrir conexão com o banco de dados
		var connection = ConnectionFactory.getConnection();
		
		//escrever o comando SQL que será executado no banco de dados
		var query = "SELECT * FROM categoria ORDER BY nome ASC";
		
		//executando o comando no banco de dados
		var statement = connection.prepareStatement(query);
		var result = statement.executeQuery(); //capturando o retorno da consulta
		
		//criando uma lista de objetos da classe Categoria
		var lista = new ArrayList<Categoria>();
		
		//percorrer a variável result para ler os registros obtidos da consulta
		while(result.next()) {
			var categoria = new Categoria(); //criando um objeto da classe Categoria
			categoria.setId(UUID.fromString(result.getString("id"))); //ler o valor do id da categoria trazido do banco de dados
			categoria.setNome(result.getString("nome")); //ler o valor do nome da categoria trazido do banco de dados
			
			lista.add(categoria); //adicionando a categoria na lista
		}
		
		//fechar a conexão
		connection.close();		
		return lista;
	}
	
	//método para verificar se um ID de categoria existe no banco de dados
	public boolean exists(UUID id) throws Exception {
		
		var connection = ConnectionFactory.getConnection();
		
		var query = "SELECT COUNT(id) AS qtd FROM categoria WHERE id = ?";
		
		var statement = connection.prepareStatement(query);
		statement.setObject(1, id);
		var result = statement.executeQuery();
		
		var qtd = 0;
		
		if(result.next()) {
			qtd = result.getInt("qtd");			
		}
		
		connection.close();
		
		return qtd > 0; //true, false
	}
	
	//Método para consultar a quantidade total de produtos por categoria
	public List<CategoriaQtdProdutosResponseDto> groupByQuantidade() throws Exception {
	
		var query = """
					SELECT
						c.nome AS nomecategoria,
						SUM(p.quantidade) AS qtdprodutos
					FROM produto p
					INNER join categoria c
					ON c.id = p.categoria_id
					GROUP BY c.nome
				""";
		
		var connection = ConnectionFactory.getConnection();
		var statement = connection.prepareStatement(query);
		var result = statement.executeQuery();
		
		var lista = new ArrayList<CategoriaQtdProdutosResponseDto>();
		
		while(result.next()) {
			
			var dto = new CategoriaQtdProdutosResponseDto();
			dto.setNomeCategoria(result.getString("nomecategoria"));
			dto.setQtdProdutos(result.getInt("qtdprodutos"));
			
			lista.add(dto); //adicionando o dto na lista
		}
		
		connection.close();
		return lista;
	}	
}











