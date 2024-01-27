package blog.dongguabai.arthas;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dongguabai
 * @date 2024-01-15 16:41
 */
public class ConsoleDemo {
    public static void main(String[] args) {

        try {
            Terminal terminal = TerminalBuilder.terminal();
            LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();
            Set<String> pids = getRunningJavaProcesses();
            interactWithUser(reader, pids);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Set<String> getRunningJavaProcesses() throws Exception {
        Set<String> pids = new HashSet<>();
        Process process = Runtime.getRuntime().exec("jps");
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            System.out.println("Currently running Java processes：");
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                String pid = line.split(" ")[0];
                pids.add(pid);
            }
        }
        return pids;
    }

    private static void interactWithUser(LineReader reader, Set<String> pids) {
        while (true) {
            String input = reader.readLine("> ");
            if ("quit".equals(input)) {
                break;
            } else {
                System.out.println("Input：" + input);
            }
        }
    }
}