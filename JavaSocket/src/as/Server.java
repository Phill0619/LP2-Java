package as;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class Server {
    ArrayList<ClientManager> printerConnections =  new ArrayList<>();
    ArrayList<String> messages =  new ArrayList<>();
    public static void main(String[] args) {
        new Server();
    }

    public Server() {
        ServerSocket server = null;
        try {
            server = new ServerSocket(15000);
            server.setReuseAddress(true);
            while (true) {
                System.out.println("Waiting for client connections");
                Socket client = server.accept();
                System.out.println("New client connected " + client.getInetAddress().getHostAddress());
                ClientManager clientManag = new ClientManager(client, this);
                new Thread(clientManag).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}