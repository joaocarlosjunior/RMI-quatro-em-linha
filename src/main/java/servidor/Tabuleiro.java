package servidor;

import java.util.Arrays;

public class Tabuleiro {
    private final Integer QUANTIDADE_LINHAS = 9;
    private final Integer QUANTIDADE_COLUNAS = 9;
    private final String[][] tabuleiro = new String[9][9];

    public Tabuleiro(){
        this.gerarTabuleiro();
    }

    public String[][] getTabuleiro(){
        return this.tabuleiro;
    }

    public Integer getQuantidadeLinhas(){
        return this.QUANTIDADE_LINHAS;
    }

    public Integer getQuantidadeColunas(){
        return this.QUANTIDADE_COLUNAS;
    }

    private void gerarTabuleiro() {
        for (int i = 0; i < QUANTIDADE_LINHAS; i++) {
            for (int j = 0; j < QUANTIDADE_COLUNAS; j++) {
                this.tabuleiro[i][j] = " * ";
            }
        }
    }



}
