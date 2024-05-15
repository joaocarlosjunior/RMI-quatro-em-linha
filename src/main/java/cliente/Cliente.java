package cliente;

import exceptions.ColunaPreenchidaException;
import exceptions.PosicaoInvalidaException;
import shared.IJogo;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Cliente {

    private static Scanner sc = new Scanner(System.in);

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
                Thread.sleep(3000);
                verificaPartida = jogo.verificaSeTemPartida(id);
            }

            System.out.println("Segundo jogador - " + jogo.segundoJogador(id).toUpperCase());

            while (true) {
                jogar(id, jogo);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());;
        }

    }

    private static void jogar(int id, IJogo jogo) throws RemoteException{
        int meuTurno = jogo.verificaTurno(id);

        if(verificaGanhador(id, jogo)){
            sc.close();
            System.exit(1);
        }

        if (meuTurno == 1) {
            String simbolo = jogo.getSimbolo(id);
            String nome = jogo.getNomeJogador(id);
            limparConsole();
            imprimirTabuleiro(jogo.getTabuleiro());
            do {
                try {
                    System.out.println("Informe a posicao a ser jogada - " + nome.toUpperCase() + " " + simbolo + " : ");
                    int posicao = sc.nextInt();
                    jogo.lerJogada(id, posicao);
                    break;
                } catch (PosicaoInvalidaException | ColunaPreenchidaException e) {
                    System.out.println("\n" + e.getMessage());
                }finally {
                    imprimirTabuleiro(jogo.getTabuleiro());
                }
            } while (true);
        }
    }

    private static boolean verificaGanhador(int id, IJogo jogo) throws RemoteException {
        int aGanhador = jogo.verificaGanhador(id);

        if(aGanhador == 1 || aGanhador == 2 || aGanhador == 3){
            limparConsole();
            if(aGanhador == 1){
                imprimirTabuleiro(jogo.getTabuleiro());
                System.out.println("\u001B[42m" + "\u001B[30m" + "Voce Ganhou!!" + "\u001B[0m");
                return true;
            }else if(aGanhador == 2){
                imprimirTabuleiro(jogo.getTabuleiro());
                System.out.println("\u001B[41m" + "\u001B[30m" + "Voce Perdeu!!" + "\u001B[0m");
                return true;
            }else{
                imprimirTabuleiro(jogo.getTabuleiro());
                System.out.println("\u001B[47m" + "\u001B[30m" + "Empate!!" + "\u001B[0m");
                return true;
            }
        }
        return false;
    }

    private static void imprimirTabuleiro(String[][] tabuleiro){
        System.out.println(" 0  1  2  3  4  5  6  7  8 ");
        for (String[] elementos : tabuleiro) {
            for (String elemento : elementos) {
                System.out.print("\u001B[44m" + "\u001B[30m" + elemento + "\u001B[0m");
            }
            System.out.println();
        }
    }

    private static void limparConsole(){
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            }
            else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
