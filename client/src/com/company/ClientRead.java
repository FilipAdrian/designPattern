package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientRead extends Thread {
    private BufferedReader reader;
    private Socket socket;
    private Client client;
    private BufferedReader input;
    public static Player player;

    public ClientRead(Socket socket, Client client) throws IOException {
        this.socket = socket;
        this.client = client;

        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                String response = input.readLine();
                if (response.contains("<--- Game Started !  --->")) {
                    System.out.println(response);
                } else if (response.contains("NO_ROLES_AVAILABLE_GO_PLAY_SOMETHING_ELSE_YOU_DUMB_PIECE_OF_SHIT")) {
                    System.out.println("No role available");
                    socket.close();
                    break;
                } else if (response.contains("Role")) {
                    System.out.println(response);
                    String role = response.substring(response.indexOf("Role:") + 6, response.indexOf(","));
                    player = client.getPlayer(role);
                    player.setRole(role);
                    player.setId(response.substring(response.indexOf("Id:") + 4));
                } else if (response.contains("<--- NIGHT --->")) {
                    player.setDay(false);
                    System.out.println(response);
                } else if (response.contains("<--- DAY --->")) {
                    player.setDay(true);
                    System.out.println(response);
                } else if (response.contains("Player")) {
                    System.out.println(response);
                } else if (response.contains("<--- VOTE NOW --->")){
                    System.out.println(response);
                    player.setVote(true);
                } else if (response.contains("IS MAFIA")){
                    System.out.println(response);
                }else if (response.contains("WIN")){
                    System.out.println(response);
                }
            } catch (IOException ex) {
                break;
            }
        }
    }
}

