package com.example.pepsell;

import com.example.pepsell.UI.Statistic;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final UUID clientId;
    private final ArrayList<String> randomMessages;
    private long messageId = 0;

    public ClientHandler(Socket socket, ArrayList<String> randomMessages) {
        this.socket = socket;
        this.clientId = UUID.randomUUID();
        this.randomMessages = randomMessages;
    }

    @Override
    public void run() {
        sendMessages();
    }

    public List<String> sendMessages() {
        try {

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

            for (String message : randomMessages) {
                messageId++;
                long deliveredTime = System.currentTimeMillis();
                String status = "Delivered";

                if (!socket.isConnected()) {
                    status = "Undelivered";
                    deliveredTime = 0;
                }else {
                    printWriter.println(message);
                }
                Server.addMessageStat(new Statistic(messageId, clientId, message, status, deliveredTime));
            }
            printWriter.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
