package as;

import java.io.*;
import java.net.Socket;

class ClientManager implements Runnable {

    private final Socket clientSocket;
    private final Server server;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private int selectedIndexPrinter;

    public void setPrinting(boolean printing) {
        isPrinting = printing;
    }

    private boolean isPrinting;
    private boolean isPrinter;

    public ClientManager(Socket socket, Server server) {
        this.clientSocket = socket;
        this.server = server;
        this.isPrinting = false;
        this.isPrinter= false;
    }

    public void setPrinter(String message) throws IOException, InterruptedException {
        boolean allPrinterInUse = true;
        for (int i = 0 ; i < server.printerConnections.size() ; i++) {
            ClientManager printer = server.printerConnections.get(i);
            if(!printer.isPrinting) {
                this.out = new PrintWriter(printer.clientSocket.getOutputStream(), true);
                this.in = new BufferedReader(new InputStreamReader(printer.clientSocket.getInputStream()));
                server.printerConnections.get(i).setPrinting(true);
                selectedIndexPrinter = i;
                allPrinterInUse = false;
            }
        }
        if (allPrinterInUse) {
            server.messages.add(message);
            writeMessage(message);
        }
    }

    public void writeMessage (String message) throws InterruptedException, IOException {
        setPrinter(message);
        if (server.messages.size() > 0) {
            int lastIndex = server.messages.size() - 1;
            printMessage(server.messages.get(server.messages.size() - 1));
            server.messages.remove(lastIndex);
        } else {
            printMessage(message);
        }
    }

    private void printMessage(String message) throws InterruptedException {
        out.println("Printing...");
        Thread.sleep(500);
        out.println("Printing requested text: \n" + message);
        server.printerConnections.get(selectedIndexPrinter).setPrinting(false);
    }

    @Override
    public void run() {
        try (InputStream stream = clientSocket.getInputStream()) {
            boolean ativo = true;
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while (ativo)
            {
                if (stream.available() != 0)
                {
                    byte[] dados = new byte[stream.available()];
                    stream.read(dados);
                    String dadosLidos = new String(dados);
                    if (dadosLidos.equals("/exit"))
                        ativo = false;
                    else if(dadosLidos.equals("/printer")) {
                        out.println("\nClient added as printer");
                        this.isPrinter = true;
                        server.printerConnections.add(this);
                    }
                    else {
                        writeMessage(new String(dados));
                    }
                }
                Thread.sleep(10);
            }
            System.out.println("Bye bye");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null)
                    in.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}