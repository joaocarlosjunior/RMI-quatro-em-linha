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
    private int contadorJogadas = 0;

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
        contadorJogadas++;
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

        if(contadorJogadas < 7){
            return 0;
        }

        if(this.partida.verificaEmpate()){
            return 3;
        }

        Jogador jogador;
        try {
            jogador = getJogadorById(id);
        } catch (JogadorInexistenteException e) {
            throw new RuntimeException(e);
        }

        Jogador jogadorGanhador = this.partida.verificaVitoria();
        if (jogadorGanhador != null) {
            return jogadorGanhador == jogador? 1: 2;
        }

        return 0;
    }

    @Override
    public String[][] getTabuleiro() throws RemoteException {
        return this.partida.getTabuleiro().getTabuleiro();
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
