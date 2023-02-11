import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Server {
    private final List<Socket> userSocket = new ArrayList<>();

    private StringBuilder randomNameGenerator() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(alphabet.length());
            name.append(alphabet.charAt(index));
        }
        return name;
    }

    public void handle(Socket socket) {
        String name = String.valueOf(randomNameGenerator());
        System.out.printf("Подключен клиент: %s%n", name);
        userSocket.add(socket);
        try (socket;
             Scanner reader = getReader(socket);
             PrintWriter writer = getWriter(socket)
        ) {
            sendResponse("Привет " + name, writer);

            while (true) {
                String message = reader.nextLine().strip();
                if (isEmptyMsg(message) || isQuitMsg(message)) {
                    for (Socket num : userSocket) {
                        if (num != socket) {
                            String userMassage = "Пользователь " + name + " вышел";
                            PrintWriter writer1 = getWriter(num);
                            sendResponse(userMassage, writer1);
                        }
                    }
                    break;
                }
                String userMassage = name + ": " + message;
                System.out.println(name + ": " + message);
                for (Socket num : userSocket) {
                    if (num != socket) {
                        PrintWriter writer1 = getWriter(num);
                        sendResponse(userMassage, writer1);
                    }
                }
            }
        } catch (NoSuchElementException e) {
            System.out.printf("Client dropped %s the connection!%n", name);
        } catch (IOException e) {
            System.out.printf("Клиент отключился: %s%n", name);
            e.printStackTrace();
        }
    }

    private void sendResponse(String response, Writer writer) throws IOException {
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