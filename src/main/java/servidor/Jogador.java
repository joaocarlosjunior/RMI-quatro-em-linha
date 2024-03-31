package servidor;

public class Jogador {
    private String nome;
    private Integer id;
    private String simbolo;
    private Partida partida;
    private String color;
    private final String ANSI_RESET = "\u001B[0m";

    public Jogador(Integer id, String nome, Partida partida) {
        this.nome = nome;
        this.id = id;
        this.partida = partida;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSimbolo() {
        return this.color + simbolo + this.ANSI_RESET;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }
}
