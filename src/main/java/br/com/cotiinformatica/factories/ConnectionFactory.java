package br.com.cotiinformatica.factories;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

	public static Connection getConnection() throws Exception {

		// parâmetros para conexão com o banco de dados
		var host = "jdbc:postgresql://postgres:5432/bd_produtosapi";
		var user = "admin";
		var pass = "senha123";

		// retornar uma conexão
		return DriverManager.getConnection(host, user, pass);
	}
}
