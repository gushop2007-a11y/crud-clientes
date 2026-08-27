CRUD de Clientes — Java + MySQL

Sistema de cadastro (CRUD) de clientes desenvolvido em Java, com persistência de dados em um banco MySQL via JDBC. Projeto pessoal de portfólio, com foco em aplicar Orientação a Objetos, comandos SQL e boas práticas de segurança.

🚀 Funcionalidades
Inserir novos clientes (nome e e-mail)
Listar todos os clientes cadastrados
Atualizar dados de um cliente existente
Deletar um cliente pelo ID

Todas as operações são realizadas através de um menu interativo no console.

🛠️ Tecnologias utilizadas
Java — linguagem principal
Maven — gerenciamento de dependências
JDBC — conectividade entre Java e o banco de dados
MySQL — banco de dados relacional
MySQL Connector/J — driver JDBC para MySQL
📁 Estrutura do projeto
src/main/java/org/example/
├── Main.java          → menu principal (ponto de entrada)
├── Cliente.java        → classe modelo (representa um cliente)
├── ClienteDAO.java     → acesso ao banco de dados (SQL)
└── ConexaoBD.java      → gerenciamento da conexão com o MySQL
🗄️ Banco de dados
sql
CREATE DATABASE crud_app;
USE crud_app;

CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100),
    email VARCHAR(100)
);
🔒 Segurança

A senha do banco de dados não fica exposta no código-fonte. Ela é armazenada em um arquivo local config.properties, que é ignorado pelo Git (.gitignore) e nunca é enviado ao repositório. A classe ConexaoBD lê essa senha em tempo de execução. Além disso, todas as consultas SQL utilizam PreparedStatement, prevenindo ataques de SQL Injection.

▶️ Como executar
Ter o MySQL Server instalado e em execução
Executar o script SQL acima para criar o banco e a tabela
Criar um arquivo config.properties na raiz do projeto com o conteúdo: senha=sua_senha_aqui
Abrir o projeto em uma IDE com suporte a Maven (ex: IntelliJ IDEA)
Aguardar o Maven baixar as dependências
Executar a classe Main.java
📚 Conceitos aplicados

Programação Orientada a Objetos (encapsulamento, sobrecarga de construtores), padrão DAO (Data Access Object), PreparedStatement e prevenção de SQL Injection, try-with-resources para gerenciamento de conexões, e boas práticas de segurança na proteção de credenciais.

👤 Autor

Gustavo Carvalho da Silva — Estudante de Análise e Desenvolvimento de Sistemas na USCS. LinkedIn
