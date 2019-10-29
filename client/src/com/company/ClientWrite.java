package com.company;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientWrite extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private Client client;
    private PrintWriter output;
    private Player player;


    public ClientWrite(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String userInput;
        try {
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        output.println(scanner.nextLine());
        while (!(userInput = scanner.nextLine()).equals("stop")) {
            try {
                if (ClientRead.player != null) {
                    player = ClientRead.player;
                    if (player.getDay() && !player.getVote()) {
                        output = new PrintWriter(socket.getOutputStream(), true);
                        output.println(userInput);
                    }
                    if (player.getVote() && player.getDay()) {
                        output = new PrintWriter(socket.getOutputStream(), true);
                        output.println(userInput);
                        player.setVote(false);
                    }
                    if (player.getRole().equals("Mafia") && !player.getDay()) {
                        output = new PrintWriter(socket.getOutputStream(), true);
                        output.println("MAFIA: " + userInput);
                    }
                    if (player.getRole().equals("Cop") && !player.getDay() && player.choose()) {
                        output = new PrintWriter(socket.getOutputStream(), true);
                        output.println("COP: " + userInput);
                    }
                } else
                    break;
            } catch (IOException e) {
                break;
            }
        }
        try {
            socket.close();
        } catch (IOException ex) {
            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
}
