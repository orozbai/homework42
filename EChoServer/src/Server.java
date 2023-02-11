import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Server {
    public void handle(Socket socket) {
        System.out.printf("Подключен клиент: %s%n", socket);

        try (socket;
             Scanner reader = getReader(socket);
             PrintWriter writer = getWriter(socket)
        ) {
            sendRespone("Привет " + socket, writer);

            while (true) {
                String message = reader.nextLine().strip();
                if (isEmptyMsg(message) || isQuitMsg(message)) {
                    break;
                }
                System.out.println(message);
                sendRespone(message.toUpperCase(), writer);
            }
        } catch (NoSuchElementException e) {
            System.out.printf("Client dropped the connection!%n");
        } catch (IOException e) {
            System.out.printf("Клиент отключился: %s%n", socket);
            e.printStackTrace();
        }
    }

    private void sendRespone(String response, Writer writer) throws IOException{
        writer.write(response);
        writer.write(System.lineSeparator());
        writer.flush();
    }

    private boolean isEmptyMsg(String message) {
        return message == null || message.isBlank();
    }

    private boolean isQuitMsg(String message) {
        return "bye".equalsIgnoreCase(message);
    }

    private Scanner getReader(Socket socket) throws IOException {
        InputStream stream = socket.getInputStream();
        InputStreamReader input = new InputStreamReader(stream, StandardCharsets.UTF_8);
        return new Scanner(input);
    }

    private PrintWriter getWriter(Socket socket) throws IOException {
        OutputStream stream = socket.getOutputStream();
        return new PrintWriter(stream);
    }
}
