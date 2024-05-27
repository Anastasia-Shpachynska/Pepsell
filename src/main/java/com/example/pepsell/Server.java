package com.example.pepsell;

import com.example.pepsell.UI.ServerUI;
import com.example.pepsell.UI.Statistic;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final int NUM_CLIENTS = 3;
    private static final int NUM_MESSAGES = 1000;

    private static final ArrayList<String> randomMessages = new ArrayList<>();
    private static final ArrayList<Statistic> statistics = new ArrayList<>();


    private static final ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private static final Object lock = new Object();
    private static int connectedClients = 0;
    private static ServerUI serverUI;


    public static void main(String[] args) {

        generateMessages();
            serverUI = new ServerUI();

            try {
                ServerSocket serverSocket = new ServerSocket(8080);

                while (connectedClients < NUM_CLIENTS) {
                    Socket socket = serverSocket.accept();
                    ClientHandler handler = new ClientHandler(socket, randomMessages);
                    clientHandlers.add(handler);

                    new Thread(() -> {
                        synchronized (lock) {
                            connectedClients++;
                            if (connectedClients == NUM_CLIENTS) {
                                lock.notifyAll();
                            } else {
                                while (connectedClients < NUM_CLIENTS) {
                                    try {
                                        lock.wait();
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        }
                        handler.run();
                    }).start();
                }

                serverSocket.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    private static void generateMessages() {
        for (int i = 1; i <= NUM_MESSAGES; i++) {
            randomMessages.add(RandomStringUtils.randomAlphabetic(10));
        }
    }

    public static synchronized void addMessageStat(Statistic statistic) {
        statistics.add(statistic);
        serverUI.updateTable(statistics);
    }

    public static ArrayList<Statistic> getMessageStat() {
        return statistics;
    }
}
