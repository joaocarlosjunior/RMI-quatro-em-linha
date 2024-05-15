package servidor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Partida {

    private Tabuleiro tabuleiro;
    private boolean partidaDisponivel;
    private Jogador jogador1, jogador2;
    private Jogador jogadorAtual;

    public Partida() {
        this.partidaDisponivel = false;
        this.tabuleiro = new Tabuleiro();
    }

    public Jogador getJogador1() {
        return jogador1;
    }

    public void setJogador1(Jogador jogador1) {
        this.jogador1 = jogador1;
        this.jogador1.setSimbolo(" x ");
        this.jogador1.setColor("\u001B[33m");
        this.jogadorAtual = this.jogador1;
    }

    public Jogador getJogador2() {
        return jogador2;
    }

    public void setJogador2(Jogador jogador2) {
        this.jogador2 = jogador2;
        this.partidaDisponivel = true;
        this.jogador2.setSimbolo(" o ");
        this.jogador2.setColor("\u001B[31m");
    }

    public boolean isPartidaDisponivel() {
        return partidaDisponivel;
    }

    public void inserirJogada(int numeroPosicaojogada, Jogador jogador){

        this.jogadorAtual = jogador;

        for (int i = this.tabuleiro.getQuantidadeLinhas() - 1; i >= 0; i--) {
            if (this.tabuleiro.getTabuleiro()[i][numeroPosicaojogada].equals(" * ")) {
                this.tabuleiro.getTabuleiro()[i][numeroPosicaojogada] = jogador.getSimbolo();
                changeTurn();
                break;
            }
        }
    }

    public Jogador verificaVitoria() {
        Jogador jogador;

        jogador = verificaVertical();
        if(jogador != null){
            return jogador;
        }

        jogador = verificaHorizontal();
        if(jogador != null){
            return jogador;
        }

        jogador = verificaDiagonalEsquerda();
        if(jogador != null){
            return jogador;
        }

        jogador = verificaDiagonalDireita();
        if(jogador != null){
            return jogador;
        }
        return null;
    }

    public boolean verificaEmpate() {
        List<String> list = Stream.of(this.tabuleiro.getTabuleiro()).flatMap(Arrays::stream).toList();
        return !list.contains(" * ");
    }

    private Jogador verificaVertical() {
        String simbolo = "";
        int count = 0;

        for (int i = this.tabuleiro.getQuantidadeLinhas() - 1; i >= 3; i--) {
            for (int j = 0; j < this.tabuleiro.getQuantidadeColunas(); j++) {

                if (!this.tabuleiro.getTabuleiro()[i][j].equals(" * ")) {
                    simbolo = this.tabuleiro.getTabuleiro()[i][j];
                    count++;
                } else {
                    continue;
                }

                for (int k = i - 1; k >= 0; k--) {
                    if (this.tabuleiro.getTabuleiro()[k][j].equals(simbolo)) {
                        simbolo = this.tabuleiro.getTabuleiro()[k][j];
                        count++;
                        if (count == 4) {
                            return this.tabuleiro.getTabuleiro()[k][j].equals(this.jogador1.getSimbolo())? this.jogador1: this.jogador2;
                        }
                    } else {
                        count = 0;
                        break;
                    }
                }

            }
        }
        return null;
    }

    private Jogador verificaHorizontal() {
        String simboloAtual = "";

        for (int i = this.tabuleiro.getQuantidadeLinhas() - 1; i >= 0; i--) {
            int count = 0;
            for (int j = 0; j < this.tabuleiro.getQuantidadeColunas(); j++) {
                if (!this.tabuleiro.getTabuleiro()[i][j].equals(" * ")) {
                    simboloAtual = this.tabuleiro.getTabuleiro()[i][j];
                    count++;
                } else {
                    continue;
                }

                if (count == 4) {
                    return this.tabuleiro.getTabuleiro()[i][j].equals(this.jogador1.getSimbolo())? this.jogador1: this.jogador2;
                } else if (j == this.tabuleiro.getQuantidadeColunas() - 1) {
                    if (!this.tabuleiro.getTabuleiro()[i][j].equals(simboloAtual)) {
                        count = 0;
                    }
                } else {
                    if (!this.tabuleiro.getTabuleiro()[i][j + 1].equals(simboloAtual)) {
                        count = 0;
                    }
                }

            }
        }
        return null;
    }

    private Jogador verificaDiagonalEsquerda() {
        String simboloAtual = "";
        int count = 0;
        for (int i = this.tabuleiro.getQuantidadeLinhas() - 1; i >= 3; i--) {
            for (int j = 3; j < this.tabuleiro.getQuantidadeColunas(); j++) {

                if (!this.tabuleiro.getTabuleiro()[i][j].equals(" * ")) {
                    simboloAtual = this.tabuleiro.getTabuleiro()[i][j];
                    count++;
                } else {
                    continue;
                }

                int linhaAnterior = i;
                for (int k = j; k >= 0; k--) {
                    linhaAnterior = linhaAnterior - 1;
                    if (this.tabuleiro.getTabuleiro()[linhaAnterior][k - 1].equals(simboloAtual)) {
                        simboloAtual = this.tabuleiro.getTabuleiro()[linhaAnterior][k - 1];
                        count++;
                        if (count == 4) {
                            return this.tabuleiro.getTabuleiro()[linhaAnterior][k - 1].equals(this.jogador1.getSimbolo())? this.jogador1: this.jogador2;
                        }
                    } else {
                        count = 0;
                        break;
                    }
                }

            }
        }
        return null;
    }

    private Jogador verificaDiagonalDireita() {
        String simboloAtual = "";
        int count = 0;
        for (int i = this.tabuleiro.getQuantidadeLinhas() - 1; i >= 3; i--) {
            for (int j = 5; j >= 0; j--) {

                if (!this.tabuleiro.getTabuleiro()[i][j].equals(" * ")) {
                    simboloAtual = this.tabuleiro.getTabuleiro()[i][j];
                    count++;
                } else {
                    continue;
                }

                int linhaAnterior = i;
                for (int k = j; k <= this.tabuleiro.getQuantidadeColunas(); k++) {
                    linhaAnterior = linhaAnterior - 1;
                    if (this.tabuleiro.getTabuleiro()[linhaAnterior][k + 1].equals(simboloAtual)) {
                        simboloAtual = this.tabuleiro.getTabuleiro()[linhaAnterior][k + 1];
                        count++;
                        if (count == 4) {
                            return this.tabuleiro.getTabuleiro()[linhaAnterior][k + 1].equals(this.jogador1.getSimbolo())? this.jogador1: this.jogador2;
                        }
                    } else {
                        count = 0;
                        break;
                    }
                }

            }
        }
        return null;
    }

    public Tabuleiro getTabuleiro() {
        return this.tabuleiro;
    }

    public Jogador getJogadorAtual() {
        return this.jogadorAtual;
    }

    private void changeTurn() {
        this.jogadorAtual = (this.jogadorAtual == this.jogador1 ? this.jogador2 : this.jogador1);
    }
}
