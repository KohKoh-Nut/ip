import java.io.IOException;
import java.io.Reader;

import commands.Command;
import session.Session;

/** Runs Zabud, a command-line assistant that stores tasks for the current session. */
public class Zabud {
    /** The line printed between sections of the command-line interface. */
    private static final String SEPARATOR = "____________________________________________________________";

    /** Prevents instantiation of this application entry-point class. */
    private Zabud() {
    }

    /**
     * Starts Zabud and delegates each line of input to the command dispatcher.
     *
     * @param args command-line arguments; not used by this application
     */
    public static void main(String[] args) {
        Session session = new Session();
        printWelcome();

        if (System.console() == null) runPipedInput(session);
        else runInteractiveInput(session);
    }

    /** Processes line-oriented input used by tests, pipes, and redirected input. */
    private static void runPipedInput(Session session) {
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                session.recordCommand(line);
                if (!Command.invoke(line, session)) break;
            }
        } catch (IOException exception) {
            System.out.println(" I could not read input: " + exception.getMessage());
        }
    }

    /** Runs a small raw-mode line editor with history and escape-key controls. */
    private static void runInteractiveInput(Session session) {
        setTerminalMode(false);
        try {
            Reader reader = new java.io.InputStreamReader(System.in);
            StringBuilder current = new StringBuilder();
            System.out.print("> ");
            int character;
            while ((character = reader.read()) != -1) {
                if (character == '\n' || character == '\r') {
                    String line = current.toString();
                    System.out.print("\n" + SEPARATOR + "\n");
                    session.recordCommand(line);
                    if (!Command.invoke(line, session)) break;
                    current.setLength(0);
                    System.out.print("> ");
                } else if (character == 27) {
                    handleEscape(reader, current, session);
                } else if (character == 127 || character == 8) {
                    if (current.length() > 0) { current.deleteCharAt(current.length() - 1); redraw(current); }
                } else if (!Character.isISOControl(character)) {
                    current.append((char) character);
                    System.out.print((char) character);
                }
            }
        } catch (IOException exception) {
            System.out.println("\n I could not read input: " + exception.getMessage());
        } finally { setTerminalMode(true); }
    }

    /** Interprets arrow-key history navigation or clears the line for a bare escape. */
    private static void handleEscape(Reader reader, StringBuilder current, Session session) throws IOException {
        reader.ready();
        if (!reader.ready()) { current.setLength(0); redraw(current); return; }
        int bracket = reader.read();
        if (bracket != '[' || !reader.ready()) { current.setLength(0); redraw(current); return; }
        int direction = reader.read();
        if (direction == 'A') current.replace(0, current.length(), session.previousCommand());
        else if (direction == 'B') current.replace(0, current.length(), session.nextCommand());
        else current.setLength(0);
        redraw(current);
    }

    /** Replaces the visible input line with the current editor value. */
    private static void redraw(StringBuilder current) {
        System.out.print("\r\033[2K> " + current);
    }

    /** Switches the terminal between canonical and raw input modes when available. */
    private static void setTerminalMode(boolean restore) {
        try {
            new ProcessBuilder("sh", "-c", restore ? "stty sane" : "stty -icanon -echo").inheritIO().start().waitFor();
        } catch (IOException | InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }

    /** Prints the welcome banner and greeting. */
    private static void printWelcome() {
        String banner =
                "███████╗  █████╗  ██████╗  ██╗   ██╗ ██████╗\n" +
                "╚══███╔╝ ██╔══██╗ ██╔══██╗ ██║   ██║ ██╔══██╗\n" +
                "  ███╔╝  ███████║ ██████╔╝ ██║   ██║ ██║  ██║\n" +
                " ███╔╝   ██╔══██║ ██╔══██╗ ██║   ██║ ██║  ██║\n" +
                "███████╗ ██║  ██║ ██████╔╝ ╚██████╔╝ ██████╔╝\n" +
                "╚══════╝ ╚═╝  ╚═╝ ╚═════╝   ╚═════╝  ╚═════╝";
        System.out.println(SEPARATOR);
        System.out.println(banner);
        System.out.println("Hello, King Solomon! I'm Zabud, your royal assistant.");
        System.out.println("What can I do for you?");
        System.out.println(SEPARATOR);
    }
}
