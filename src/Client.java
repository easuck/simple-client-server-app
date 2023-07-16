import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception{

        Socket socket = new Socket("localhost", 8080);
        Scanner scanner = new Scanner(System.in);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        while (scanner.hasNextLine()){
            String clientMessage = scanner.nextLine();
            printWriter.println(clientMessage);
            printWriter.flush();
        }
    }
}