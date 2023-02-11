import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {

    private final ExecutorService pool = Executors.newCachedThreadPool();

    private final int port;

    private EchoServer(int port) {
        this.port = port;
    }

    static EchoServer bindToPort(int port) {
        return new EchoServer(port);
    }

    public void run() {
        Server server1 = new Server();
        System.out.println("Сервер включен");
        try (ServerSocket server = new ServerSocket(port)) {
            while (!server.isClosed()) {
                Socket clientSocket = server.accept();
                pool.submit(() -> server1.handle(clientSocket));
            }
        } catch (IOException e) {
            String fmtMsg = "Вероятнее всего порт %s занят.%n";
            System.out.printf(fmtMsg, port);
            e.printStackTrace();
        }
    }
}