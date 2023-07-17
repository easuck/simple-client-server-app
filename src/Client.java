import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {

    public static List<String> chatHistory = new ArrayList<>();
    private Socket socket;
    private ClientHandler clientHandler;
    private Scanner scanner;
    private PrintWriter printWriter;
    private String username;

    public Client(Socket socket, String username){
        try{
            this.socket = socket;
            scanner = new Scanner(socket.getInputStream());
            printWriter = new PrintWriter(socket.getOutputStream());
            this.username = username;
        }
        catch(IOException e){
            closeEverything(socket, scanner, printWriter);
        }
    }

    public void sendMessage(){
        try{
            printWriter.println(username);
            printWriter.flush();

            Scanner scannerClientMessages = new Scanner(System.in);
            while (socket.isConnected()){
                String message = scannerClientMessages.nextLine();
                if (message.equals("/history")){
                    for (String m : chatHistory){
                        System.out.println(m);
                    }
                }
                else{
                    chatHistory.add(username + ": " + message);
                    printWriter.println(username + ": " + message);
                    printWriter.flush();
                }

            }
        }
        catch(Exception e){
            closeEverything(socket, scanner, printWriter);
        }
    }

    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromGroupChat;
                while(socket.isConnected()){
                    try{
                        messageFromGroupChat = scanner.nextLine();
                        chatHistory.add(messageFromGroupChat);
                        System.out.println(messageFromGroupChat);
                    }
                    catch(Exception e){
                        closeEverything(socket, scanner, printWriter);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, Scanner scanner, PrintWriter printWriter){
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

    public static void main(String[] args) throws IOException{
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        Socket socket = new Socket("localhost", 8080);
        Client client = new Client(socket, username);
        client.listenForMessage();
        client.sendMessage();
    }
}