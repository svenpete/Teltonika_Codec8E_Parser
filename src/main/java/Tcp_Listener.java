/*
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Tcp_Listener {

    ServerSocket serverSocket = new ServerSocket(port);
    Socket clientSocket = serverSocket.accept();
    InputStream inputStream = clientSocket.getInputStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    String line = reader.readLine();
            System.out.println("From Client: " + line);

    byte response = (byte)0x01;
    OutputStream output = clientSocket.getOutputStream();
            output.write(response);
            output.flush();
}

 */
