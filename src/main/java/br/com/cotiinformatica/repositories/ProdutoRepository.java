package br.com.cotiinformatica.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.cotiinformatica.entities.Categoria;
import br.com.cotiinformatica.entities.Produto;
import br.com.cotiinformatica.factories.ConnectionFactory;

public class ProdutoRepository {

	// método para inserir um produto no banco de dados
	public void insert(Produto produto, UUID categoriaId) throws Exception {

		var connection = ConnectionFactory.getConnection();

		var query = "INSERT INTO produto(id, nome, preco, quantidade, categoria_id) VALUES(?,?,?,?,?)";

		var statement = connection.prepareStatement(query);
		statement.setObject(1, produto.getId());
		statement.setString(2, produto.getNome());
		statement.setDouble(3, produto.getPreco());
		statement.setInt(4, produto.getQuantidade());
		statement.setObject(5, categoriaId);
		statement.execute();

		connection.close();
	}

	// método para atualizar um produto no banco de dados
	public void update(Produto produto, UUID categoriaId) throws Exception {

		var connection = ConnectionFactory.getConnection();

		var query = "UPDATE produto SET nome=?, preco=?, quantidade=?, categoria_id=? WHERE id=?";

		var statement = connection.prepareStatement(query);
		statement.setString(1, produto.getNome());
		statement.setDouble(2, produto.getPreco());
		statement.setInt(3, produto.getQuantidade());
		statement.setObject(4, categoriaId);
		statement.setObject(5, produto.getId());
		statement.execute();

		connection.close();
	}

	// método para excluir um produto no banco de dados
	public void delete(UUID id) throws Exception {

		var connection = ConnectionFactory.getConnection();

		var query = "DELETE FROM produto WHERE id=?";

		var statement = connection.prepareStatement(query);
		statement.setObject(1, id);
		statement.execute();

		connection.close();
	}

	// método para consultar os produtos pelo nome
	public List<Produto> findAll(String nome) throws Exception {

		var connection = ConnectionFactory.getConnection();

		var query = """
					SELECT
						p.id as produto_id,
						p.nome as produto_nome,
						p.preco,
						p.quantidade,
						c.id as categoria_id,
						c.nome as categoria_nome
					FROM produto p
					INNER JOIN categoria c
					ON c.id = p.categoria_id
					WHERE p.nome ILIKE ?
					ORDER BY p.nome
				""";

		var statement = connection.prepareStatement(query);
		statement.setString(1, "%" + nome + "%");
		var result = statement.executeQuery();

		var lista = new ArrayList<Produto>();
		while (result.next()) {

			var produto = new Produto(); // instanciando a classe Produto
			produto.setCategoria(new Categoria()); // instanciando a classe Categoria

			produto.setId(UUID.fromString(result.getString("produto_id")));
			produto.setNome(result.getString("produto_nome"));
			produto.setPreco(result.getDouble("preco"));
			produto.setQuantidade(result.getInt("quantidade"));
			produto.getCategoria().setId(UUID.fromString(result.getString("categoria_id")));
			produto.getCategoria().setNome(result.getString("categoria_nome"));

			lista.add(produto);
		}

		connection.close();
		return lista;
	}

	// método para consultar 1 produto pelo id
	public Produto findById(UUID id) throws Exception {

		var connection = ConnectionFactory.getConnection();

		var query = """
					SELECT
						p.id as produto_id,
						p.nome as produto_nome,
						p.preco,
						p.quantidade,
						c.id as categoria_id,
						c.nome as categoria_nome
					FROM produto p
					INNER JOIN categoria c
					ON c.id = p.categoria_id
					WHERE p.id = ?
				""";

		var statement = connection.prepareStatement(query);
		statement.setObject(1, id);
		var result = statement.executeQuery();

		Produto produto = null;
		
		if (result.next()) {

			produto = new Produto(); // instanciando a classe Produto
			produto.setCategoria(new Categoria()); // instanciando a classe Categoria

			produto.setId(UUID.fromString(result.getString("produto_id")));
			produto.setNome(result.getString("produto_nome"));
			produto.setPreco(result.getDouble("preco"));
			produto.setQuantidade(result.getInt("quantidade"));
			produto.getCategoria().setId(UUID.fromString(result.getString("categoria_id")));
			produto.getCategoria().setNome(result.getString("categoria_nome"));
		}

		connection.close();
		return produto;
	}

	// método para verificar se um ID de produto existe no banco de dados
	public boolean exists(UUID id) throws Exception {

		var connection = ConnectionFactory.getConnection();

		var query = "SELECT COUNT(id) AS qtd FROM produto WHERE id = ?";

		var statement = connection.prepareStatement(query);
		statement.setObject(1, id);
		var result = statement.executeQuery();

		var qtd = 0;

		if (result.next()) {
			qtd = result.getInt("qtd");
		}

		connection.close();

		return qtd > 0; // true, false
	}

}
