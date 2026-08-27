package org.example;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        ClienteDAO dao = new ClienteDAO();
        Scanner sc = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n1-Inserir 2-Listar 3-Atualizar 4-Deletar 5-Sair");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Nome: ");
                    String nome = sc.nextLine();
                    System.out.print("Email: ");
                    String email = sc.nextLine();
                    dao.inserir(new Cliente(nome, email));
                    System.out.println("Cliente inserido!");
                    break;
                case 2:
                    List<Cliente> lista = dao.listar();
                    for (Cliente c : lista) {
                        System.out.println(c.getId() + " - " + c.getNome() + " - " + c.getEmail());
                    }
                    break;
                case 3:
                    System.out.print("ID: ");
                    int idAtt = sc.nextInt(); sc.nextLine();
                    System.out.print("Novo nome: ");
                    String novoNome = sc.nextLine();
                    System.out.print("Novo email: ");
                    String novoEmail = sc.nextLine();
                    dao.atualizar(new Cliente(idAtt, novoNome, novoEmail));
                    System.out.println("Cliente atualizado!");
                    break;
                case 4:
                    System.out.print("ID: ");
                    int idDel = sc.nextInt();
                    dao.deletar(idDel);
                    System.out.println("Cliente deletado!");
                    break;
            }
        } while (opcao != 5);
    }
}