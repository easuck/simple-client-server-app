import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientHandler implements Runnable{

    public static List<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private Scanner scanner;
    private PrintWriter printWriter;
    private String clientUsername;

    public ClientHandler(Socket socket){
        try{
            this.socket = socket;
            scanner = new Scanner(socket.getInputStream());
            printWriter = new PrintWriter(socket.getOutputStream());
            clientUsername = scanner.nextLine();
            clientHandlers.add(this);
            broadcastMessage("Server: client " + clientUsername + " has joined the chat");
        }
        catch(IOException e){
            closeEverything(socket, scanner, printWriter);
        }
    }


    @Override
    public void run() {
        while (socket.isConnected()){
            try{
                String messageFromClient = scanner.nextLine();
                broadcastMessage(messageFromClient);
            }
            catch(Exception e){
                closeEverything(socket, scanner, printWriter);
                break;
            }
        }
    }

    public void broadcastMessage(String message) {
        for (ClientHandler clientHandler : clientHandlers){
            try{
                if (!clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.printWriter.println(message);
                    clientHandler.printWriter.flush();
                }
            }
            catch (Exception e){
                closeEverything(socket, scanner, printWriter);
            }
        }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("Server: " + clientUsername + " has left the chat");
    }

    public void closeEverything(Socket socket, Scanner scanner, PrintWriter printWriter){
        removeClientHandler();
        try{
            if (scanner != null){
                scanner.close();
            }
            if (printWriter != null){
                printWriter.close();
            }
            if (socket != null){
                socket.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
