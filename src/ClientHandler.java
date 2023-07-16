import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable{

    private Socket socket;

    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        try{
            Scanner scanner = new Scanner(socket.getInputStream());
            PrintWriter printWriter = new PrintWriter(System.out);
            while(scanner.hasNextLine()){
                printWriter.println(scanner.nextLine());
                System.out.flush();
            }
        }
        catch (Exception e){
            System.out.println("something went wrong");
        }
    }
}
