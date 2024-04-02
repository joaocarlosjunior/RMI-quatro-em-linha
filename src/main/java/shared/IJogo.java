package shared;

import exceptions.ColunaPreenchidaException;
import exceptions.PosicaoInvalidaException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IJogo extends Remote {

    void lerJogada(int id, int posicaoJogada) throws RemoteException, PosicaoInvalidaException, ColunaPreenchidaException;

    int adicionarJogador(String nome)throws RemoteException;

    int verificaSeTemPartida(int id) throws RemoteException;

    String segundoJogador(int id)throws RemoteException;

    int verificaTurno(int id)throws RemoteException;

    String[][] getTabuleiro() throws RemoteException;

    String getSimbolo(int id) throws RemoteException;

    String getNomeJogador(int id) throws RemoteException;

    int verificaGanhador(int id) throws RemoteException;

}
