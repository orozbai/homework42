import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Commands {
    public static String command(String command) {
        String sss = "";
        String[] words = command.split(" ");
        String firstWord = words[0];
        String[] allWordsAfterFirst = Arrays.copyOfRange(words, 1, words.length);
        String secondWord = String.join(" ", allWordsAfterFirst);
        Server server = new Server();
        Map<Socket, String> map = server.getUsers();
        ArrayList<String> arrayList = new ArrayList<>(map.values());
        for (String s : arrayList) {
            if (secondWord.equals(s)) {
                System.out.println("Имя не может содержать пробелы или быть похожим на другого пользователя");
                secondWord = " ";
                break;
            }
        }
        if (!secondWord.contains(" ")) {
            switch (firstWord) {
                case "/name":
                    return secondWord;
                case "/list":
                    return "list";
            }
        } else if (firstWord.contains("/whisper")) {
            return secondWord;
        } else {
            System.out.println("Имя не может содержать пробелы или быть похожим на другого пользователя");
        }
        return sss;
    }
}