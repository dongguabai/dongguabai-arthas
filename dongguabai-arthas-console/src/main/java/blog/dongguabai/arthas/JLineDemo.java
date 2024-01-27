package blog.dongguabai.arthas;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

/**
 * @author dongguabai
 * @date 2024-01-15 16:33
 */
public class JLineDemo {

    public static void main(String[] args) throws Exception {
        Terminal terminal = TerminalBuilder.builder().system(true).build();
        LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).build();

        String line;
        while (true) {
            line = lineReader.readLine("> ");
            System.out.println("Input: " + line);
            if ("quit".equalsIgnoreCase(line)) {
                break;
            }
        }
    }
}