import java.util.Scanner;

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
        TaskList taskList = new TaskList();
        printWelcome();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            System.out.println(SEPARATOR);
            if (!Command.invoke(scanner.nextLine(), taskList)) {
                break;
            }
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
