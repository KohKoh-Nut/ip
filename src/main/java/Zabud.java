import java.util.Scanner;

public class Zabud {
    public static void main(String[] args) {
        String separator = "____________________________________________________________";
        String banner =
            "███████╗  █████╗  ██████╗  ██╗   ██╗ ██████╗\n" +
            "╚══███╔╝ ██╔══██╗ ██╔══██╗ ██║   ██║ ██╔══██╗\n" +
            "  ███╔╝  ███████║ ██████╔╝ ██║   ██║ ██║  ██║\n" +
            " ███╔╝   ██╔══██║ ██╔══██╗ ██║   ██║ ██║  ██║\n" +
            "███████╗ ██║  ██║ ██████╔╝ ╚██████╔╝ ██████╔╝\n" +
            "╚══════╝ ╚═╝  ╚═╝ ╚═════╝   ╚═════╝  ╚═════╝";

        System.out.println(separator);
        System.out.println(banner);
        System.out.println("Hello, King Solomon! I'm Zabud, your royal assistant.");
        System.out.println("What can I do for you?");
        System.out.println(separator);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(separator);
            System.out.println(" " + command);
            System.out.println(separator);

            if (command.equals("bye")) {
                System.out.println("Bye, King Solomon. Hope to see you again soon!");
                System.out.println(separator);
                break;
            }
        }
    }
}
