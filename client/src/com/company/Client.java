package com.company;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private String hostname;
    private int port;

    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public static void main(String[] args) {
        Client client = new Client("192.168.103.102", 6666);
        client.execute();
    }

    public void execute() {
        try {
            Socket socket = new Socket(hostname, port);
            new ClientRead(socket, this).start();
            new ClientWrite(socket, this).start();

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }


    Player getPlayer(String role) {
        Factory factory = new Factory();
        switch (role) {
            case "Cop": {
                return factory.GetPlayer(PlayerType.Cop);
            }
            case "Citizen": {
                return factory.GetPlayer(PlayerType.Citizen);
            }
            case "Mafia": {
                return factory.GetPlayer(PlayerType.Mafia);
            }
        }
        throw new RuntimeException();
    }
}
