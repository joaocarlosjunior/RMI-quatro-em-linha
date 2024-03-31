package servidor;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Servidor {
    public static void main(String[] args) {

        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            System.out.println("RMI Registry pronto!");

            Jogo jogo = new Jogo();

            registry.rebind("QuatroEmLinha", jogo);
            System.out.println("Servidor do jogo pronto!");
        } catch (RemoteException e) {
            System.out.println("Falha no servidor!");
        }

    }
}
