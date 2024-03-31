package servidor;

import exceptions.ColunaPreenchidaException;
import exceptions.JogadorInexistenteException;
import exceptions.PosicaoInvalidaException;
import shared.IJogo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Jogo extends UnicastRemoteObject implements IJogo {

    private Partida partida;
    private final ReadWriteLock uIDreadWriteLock = new ReentrantReadWriteLock();
    private final Lock idWriteLock = uIDreadWriteLock.writeLock();

    private final ReadWriteLock playersReadWriteLock = new ReentrantReadWriteLock();
    private final Lock playersReadLock = playersReadWriteLock.readLock();
    private final Lock playersWriteLock = playersReadWriteLock.writeLock();

    private int nextId;

    public Jogo() throws RemoteException {
        this.nextId = 0;
        this.partida = new Partida();
    }

    @Override
    public int adicionarJogador(String nome) throws RemoteException {
        idWriteLock.lock();
        playersWriteLock.lock();
        try {
            Jogador jogador = new Jogador(this.nextId++, nome, this.partida);
            System.out.println("Usuário " + jogador.getNome() + " (" + jogador.getId() + ") entrou!");
            if (nextId < 2) {
                this.partida.setJogador1(jogador);
            } else {
                this.partida.setJogador2(jogador);
            }
            return jogador.getId();
        } finally {
            idWriteLock.unlock();
            playersWriteLock.unlock();
        }

    }

    @Override
    public int verificaSeTemPartida(int id) throws RemoteException {
        Jogador jogadorAtual;
        try {
            jogadorAtual = getJogadorById(id);
        } catch (JogadorInexistenteException e) {
            return -1;
        }

        if (this.partida != null) {
            if (this.partida.isPartidaDisponivel()) {
                if (this.partida.getJogador1() == jogadorAtual) {
                    return 1;
                } else {
                    return 2;
                }
            } else {
                return -2;
            }
        }

        return 0;
    }

    @Override
    public void lerJogada(int id, int posicaoJogada) throws RemoteException, PosicaoInvalidaException, ColunaPreenchidaException {

        if (posicaoJogada < 0 || posicaoJogada > 8) {
            throw new PosicaoInvalidaException("Posicao inválida!!");
        }

        if (!this.partida.getTabuleiro().getTabuleiro()[0][posicaoJogada].equals(" * ")) {
            throw new ColunaPreenchidaException("Coluna toda preenchida! Insira em outra coluna...");
        }

        Jogador jogador;
        try {
            jogador = getJogadorById(id);
        } catch (JogadorInexistenteException e) {
            throw new RuntimeException(e);
        }

        this.partida.inserirJogada(posicaoJogada, jogador);
    }

    private Jogador getJogadorById(int id) throws JogadorInexistenteException {
        playersReadLock.lock();
        try {
            if (this.partida.getJogador1().getId() == id) {
                return this.partida.getJogador1();
            } else if (this.partida.getJogador2().getId() == id) {
                return this.partida.getJogador2();
            } else {
                throw new JogadorInexistenteException("Jogador inexistente no servidor!");
            }
        } finally {
            playersReadLock.unlock();
        }
    }

    @Override
    public String segundoJogador(int id) throws RemoteException {
        Jogador jogador;
        try {
            jogador = getJogadorById(id);
        } catch (JogadorInexistenteException e) {
            System.out.println(e.getMessage());
            return null;
        }

        if (this.partida.getJogador1() == jogador) {
            return this.partida.getJogador2().getNome();
        } else {
            return this.partida.getJogador1().getNome();
        }

    }

    @Override
    public int verificaTurno(int id) throws RemoteException {
        Jogador jogador;
        try {
            jogador = getJogadorById(id);
        } catch (JogadorInexistenteException e) {
            throw new RuntimeException(e);
        }

        if (jogador.getPartida().getJogadorAtual() == jogador) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int verificaGanhador(int id) throws RemoteException{
        Jogador jogador;
        try {
            jogador = getJogadorById(id);
        } catch (JogadorInexistenteException e) {
            throw new RuntimeException(e);
        }

        Jogador jogadorGanhador = this.partida.verificaVitoria();
        if (jogadorGanhador != null) {
            if(jogadorGanhador == jogador){
                return 1;
            }else{
                return 2;
            }
        }
        return 0;
    }

    @Override
    public String imprimirTabuleiro() throws RemoteException {

        String[][] tabuleiro = this.partida.getTabuleiro().getTabuleiro();

        return "\n 0  1  2  3  4  5  6  7  8 \n" +
                tabuleiro[0][0] + tabuleiro[0][1] + tabuleiro[0][2] + tabuleiro[0][3] + tabuleiro[0][4] + tabuleiro[0][5] + tabuleiro[0][6] + tabuleiro[0][7] + tabuleiro[0][8] + "\n" +
                tabuleiro[1][0] + tabuleiro[1][1] + tabuleiro[1][2] + tabuleiro[1][3] + tabuleiro[1][4] + tabuleiro[1][5] + tabuleiro[1][6] + tabuleiro[1][7] + tabuleiro[1][8] + "\n" +
                tabuleiro[2][0] + tabuleiro[2][1] + tabuleiro[2][2] + tabuleiro[2][3] + tabuleiro[2][4] + tabuleiro[2][5] + tabuleiro[2][6] + tabuleiro[2][7] + tabuleiro[2][8] + "\n" +
                tabuleiro[3][0] + tabuleiro[3][1] + tabuleiro[3][2] + tabuleiro[3][3] + tabuleiro[3][4] + tabuleiro[3][5] + tabuleiro[3][6] + tabuleiro[3][7] + tabuleiro[3][8] + "\n" +
                tabuleiro[4][0] + tabuleiro[4][1] + tabuleiro[4][2] + tabuleiro[4][3] + tabuleiro[4][4] + tabuleiro[4][5] + tabuleiro[4][6] + tabuleiro[4][7] + tabuleiro[4][8] + "\n" +
                tabuleiro[5][0] + tabuleiro[5][1] + tabuleiro[5][2] + tabuleiro[5][3] + tabuleiro[5][4] + tabuleiro[5][5] + tabuleiro[5][6] + tabuleiro[5][7] + tabuleiro[5][8] + "\n" +
                tabuleiro[6][0] + tabuleiro[6][1] + tabuleiro[6][2] + tabuleiro[6][3] + tabuleiro[6][4] + tabuleiro[6][5] + tabuleiro[6][6] + tabuleiro[6][7] + tabuleiro[6][8] + "\n" +
                tabuleiro[7][0] + tabuleiro[7][1] + tabuleiro[7][2] + tabuleiro[7][3] + tabuleiro[7][4] + tabuleiro[7][5] + tabuleiro[7][6] + tabuleiro[7][7] + tabuleiro[7][8] + "\n" +
                tabuleiro[8][0] + tabuleiro[8][1] + tabuleiro[8][2] + tabuleiro[8][3] + tabuleiro[8][4] + tabuleiro[8][5] + tabuleiro[8][6] + tabuleiro[8][7] + tabuleiro[8][8] + "\n" +
                "\n";
    }

    @Override
    public String getSimbolo(int id) throws RemoteException {
        Jogador jogador;
        try {
            jogador = this.getJogadorById(id);
        } catch (JogadorInexistenteException e) {
            throw new RuntimeException(e);
        }
        return jogador.getSimbolo();
    }

    @Override
    public String getNomeJogador(int id) throws RemoteException {
        Jogador jogador;
        try {
            jogador = this.getJogadorById(id);
        } catch (JogadorInexistenteException e) {
            throw new RuntimeException(e);
        }
        return jogador.getNome();
    }

}
