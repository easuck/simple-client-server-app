import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws Exception{

        ServerSocket serverSocket = new ServerSocket(8080);
        while(true){
            Socket socket = serverSocket.accept();
            System.out.println("client connected");
            Thread thread = new Thread(new ClientHandler(socket));
            thread.start();
        }
    }
}
