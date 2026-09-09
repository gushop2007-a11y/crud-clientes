package org.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ServidorWeb {

    static ClienteDAO dao = new ClienteDAO();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", ServidorWeb::paginaInicial);
        server.createContext("/inserir", ServidorWeb::inserirCliente);
        server.createContext("/editar", ServidorWeb::formularioEditar);
        server.createContext("/atualizar", ServidorWeb::atualizarCliente);
        server.createContext("/deletar", ServidorWeb::deletarCliente);

        server.start();
        System.out.println("Servidor rodando em http://localhost:8080");
    }

    static Map<String, String> lerParametros(String corpo) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (corpo == null || corpo.isEmpty()) return params;
        for (String par : corpo.split("&")) {
            String[] partes = par.split("=");
            String chave = partes[0];
            String valor = partes.length > 1 ? URLDecoder.decode(partes[1], "UTF-8") : "";
            params.put(chave, valor);
        }
        return params;
    }

    static void paginaInicial(HttpExchange exchange) throws IOException {
        List<Cliente> clientes;
        try {
            clientes = dao.listar();
        } catch (SQLException e) {
            throw new IOException("Erro ao buscar clientes: " + e.getMessage());
        }

        StringBuilder html = new StringBuilder();
        html.append(cabecalho("Clientes"));
        html.append("<h1>Lista de Clientes</h1>");
        html.append("<table><tr><th>ID</th><th>Nome</th><th>Email</th><th>Acoes</th></tr>");

        for (Cliente c : clientes) {
            html.append("<tr>");
            html.append("<td>" + c.getId() + "</td>");
            html.append("<td>" + c.getNome() + "</td>");
            html.append("<td>" + c.getEmail() + "</td>");
            html.append("<td>");
            html.append("<a class=\"btn-editar\" href=\"/editar?id=" + c.getId() + "\">Editar</a> ");
            html.append("<form class=\"form-acao\" action=\"/deletar\" method=\"POST\" onsubmit=\"return confirm('Excluir este cliente?');\">");
            html.append("<input type=\"hidden\" name=\"id\" value=\"" + c.getId() + "\">");
            html.append("<button class=\"btn-excluir\" type=\"submit\">Excluir</button>");
            html.append("</form>");
            html.append("</td>");
            html.append("</tr>");
        }

        html.append("</table>");
        html.append("<form class=\"form-principal\" action=\"/inserir\" method=\"POST\">");
        html.append("<h3>Novo Cliente</h3>");
        html.append("<input type=\"text\" name=\"nome\" placeholder=\"Nome\" required>");
        html.append("<input type=\"email\" name=\"email\" placeholder=\"Email\" required>");
        html.append("<button type=\"submit\">Adicionar</button>");
        html.append("</form>");
        html.append("</body></html>");

        responder(exchange, html.toString());
    }

    static void inserirCliente(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        String corpo = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
        Map<String, String> params = lerParametros(corpo);

        try {
            dao.inserir(new Cliente(params.get("nome"), params.get("email")));
        } catch (SQLException e) {
            throw new IOException("Erro ao inserir cliente: " + e.getMessage());
        }

        redirecionar(exchange, "/");
    }

    static void formularioEditar(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> params = lerParametros(query);
        int id = Integer.parseInt(params.get("id"));

        List<Cliente> clientes;
        try {
            clientes = dao.listar();
        } catch (SQLException e) {
            throw new IOException("Erro ao buscar cliente: " + e.getMessage());
        }

        Cliente clienteEncontrado = null;
        for (Cliente c : clientes) {
            if (c.getId() == id) {
                clienteEncontrado = c;
                break;
            }
        }

        if (clienteEncontrado == null) {
            redirecionar(exchange, "/");
            return;
        }

        StringBuilder html = new StringBuilder();
        html.append(cabecalho("Editar Cliente"));
        html.append("<h1>Editar Cliente</h1>");
        html.append("<form class=\"form-principal\" action=\"/atualizar\" method=\"POST\">");
        html.append("<input type=\"hidden\" name=\"id\" value=\"" + clienteEncontrado.getId() + "\">");
        html.append("<input type=\"text\" name=\"nome\" value=\"" + clienteEncontrado.getNome() + "\" required>");
        html.append("<input type=\"email\" name=\"email\" value=\"" + clienteEncontrado.getEmail() + "\" required>");
        html.append("<button type=\"submit\">Salvar</button>");
        html.append("</form>");
        html.append("<p><a href=\"/\">Voltar para a lista</a></p>");
        html.append("</body></html>");

        responder(exchange, html.toString());
    }

    static void atualizarCliente(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        String corpo = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
        Map<String, String> params = lerParametros(corpo);

        int id = Integer.parseInt(params.get("id"));
        String nome = params.get("nome");
        String email = params.get("email");

        try {
            dao.atualizar(new Cliente(id, nome, email));
        } catch (SQLException e) {
            throw new IOException("Erro ao atualizar cliente: " + e.getMessage());
        }

        redirecionar(exchange, "/");
    }

    static void deletarCliente(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        String corpo = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
        Map<String, String> params = lerParametros(corpo);

        int id = Integer.parseInt(params.get("id"));

        try {
            dao.deletar(id);
        } catch (SQLException e) {
            throw new IOException("Erro ao deletar cliente: " + e.getMessage());
        }

        redirecionar(exchange, "/");
    }

    static String cabecalho(String titulo) {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><meta charset=\"UTF-8\"><title>" + titulo + "</title>");
        html.append("<style>");
        html.append("body { font-family: Arial; background: #f3f4f6; padding: 40px; }");
        html.append("h1 { color: #1e3a8a; }");
        html.append("table { width: 100%; border-collapse: collapse; background: white; }");
        html.append("th, td { padding: 10px; border: 1px solid #ddd; text-align: left; }");
        html.append("th { background: #2563eb; color: white; }");
        html.append(".form-principal { margin-top: 30px; background: white; padding: 20px; border-radius: 8px; }");
        html.append(".form-principal input { padding: 8px; margin: 5px 0; width: 100%; box-sizing: border-box; }");
        html.append(".form-principal button { padding: 10px 20px; background: #2563eb; color: white; border: none; border-radius: 5px; cursor: pointer; }");
        html.append(".form-acao { display: inline; }");
        html.append(".btn-editar { color: #2563eb; text-decoration: none; margin-right: 8px; }");
        html.append(".btn-excluir { background: #dc2626; color: white; border: none; padding: 5px 10px; font-size: 13px; border-radius: 4px; cursor: pointer; }");
        html.append("</style></head><body>");
        return html.toString();
    }

    static void redirecionar(HttpExchange exchange, String destino) throws IOException {
        exchange.getResponseHeaders().add("Location", destino);
        exchange.sendResponseHeaders(302, -1);
    }

    static void responder(HttpExchange exchange, String html) throws IOException {
        byte[] resposta = html.getBytes("UTF-8");
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, resposta.length);
        OutputStream os = exchange.getResponseBody();
        os.write(resposta);
        os.close();
    }
}