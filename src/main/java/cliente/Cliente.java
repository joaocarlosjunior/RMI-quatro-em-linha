package cliente;

import exceptions.ColunaPreenchidaException;
import exceptions.PosicaoInvalidaException;
import shared.IJogo;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Cliente {


    public static void main(String[] args) {

        try {
            IJogo jogo = (IJogo) Naming.lookup("//" + args[0] + "/QuatroEmLinha");

            int id = jogo.adicionarJogador(args[1]);

            System.out.println("Procurando Partida...");

            int verificaPartida = jogo.verificaSeTemPartida(id);

            while (verificaPartida != 1 && verificaPartida != 2) {
                if (verificaPartida == -2) {
                    System.out.println("Aguardando Segundo Jogador!");
                }
                Thread.sleep(1000);
                verificaPartida = jogo.verificaSeTemPartida(id);
            }

            System.out.println("Segundo jogador " + jogo.segundoJogador(id) + " entrou ....");

            System.out.println(jogo.imprimirTabuleiro());
            while (true) {
                jogar(id,jogo);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static void jogar(int id, IJogo jogo) throws RemoteException{
        Scanner sc = new Scanner(System.in);
        int meuTurno = jogo.verificaTurno(id);
        String simbolo = jogo.getSimbolo(id);
        String nome = jogo.getNomeJogador(id);

        if (meuTurno == 1) {

            System.out.println(jogo.imprimirTabuleiro());
            do {
                try {
                    System.out.println("Informe a posicao a ser jogada - " + nome.toUpperCase() + " " + simbolo + " : ");
                    int posicao = sc.nextInt();
                    jogo.lerJogada(id, posicao);
                    break;
                } catch (PosicaoInvalidaException | ColunaPreenchidaException e) {
                    System.out.println("\n" + e.getMessage());
                }finally {
                    System.out.println(jogo.imprimirTabuleiro());
                }
            } while (true);

        } else if(meuTurno == 5){
            System.out.println(jogo.imprimirTabuleiro());
            System.out.println("Voce Ganhou!");
            sc.close();
            System.exit(1);

        }else if(meuTurno == 6 ){

            System.out.println(jogo.imprimirTabuleiro());
            System.out.println("Voce perdeu!");
            sc.close();
            System.exit(1);

        }

    }
}
