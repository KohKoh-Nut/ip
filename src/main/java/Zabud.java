import java.util.Scanner;

/**
 * Runs Zabud, a command-line assistant that stores tasks for the current session.
 */
public class Zabud {
    /** The maximum number of tasks that can be stored in one session. */
    private static final int MAX_TASKS = 100;

    /** The line printed between sections of the command-line interface. */
    private static final String SEPARATOR = "____________________________________________________________";

    /** Prevents instantiation of this application entry-point class. */
    private Zabud() {
    }

    /** Commands understood by Zabud and the action performed by each command. */
    private enum Command {
        /** Ends the current session. */
        BYE {
            @Override
            boolean execute(Session session, String input) {
                System.out.println("Bye, King Solomon. Hope to see you again soon!");
                System.out.println(SEPARATOR);
                return false;
            }
        },

        /** Displays all tasks entered in the current session. */
        LIST {
            @Override
            boolean execute(Session session, String input) {
                printTasks(session);
                return true;
            }
        },

        /** Marks the task selected by the user as done. */
        MARK {
            @Override
            boolean execute(Session session, String input) {
                try {
                    int taskNumber = Integer.parseInt(input.substring("mark ".length()).trim());
                    if (taskNumber < 1 || taskNumber > session.taskCount) {
                        System.out.println(" Task number is out of range.");
                        return true;
                    }

                    int taskIndex = taskNumber - 1;
                    session.tasks[taskIndex].markAsDone();
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + session.tasks[taskIndex]);
                } catch (NumberFormatException | StringIndexOutOfBoundsException exception) {
                    System.out.println(" Please specify a valid task number.");
                }
                return true;
            }
        },

        /** Marks the task selected by the user as not done. */
        UNMARK {
            @Override
            boolean execute(Session session, String input) {
                try {
                    int taskNumber = Integer.parseInt(input.substring("unmark ".length()).trim());
                    if (taskNumber < 1 || taskNumber > session.taskCount) {
                        System.out.println(" Task number is out of range.");
                        return true;
                    }

                    int taskIndex = taskNumber - 1;
                    session.tasks[taskIndex].markAsNotDone();
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + session.tasks[taskIndex]);
                } catch (NumberFormatException | StringIndexOutOfBoundsException exception) {
                    System.out.println(" Please specify a valid task number.");
                }
                return true;
            }
        },

        /** Adds a to-do task. */
        TODO {
            @Override
            boolean execute(Session session, String input) {
                String description = input.substring("todo".length()).trim();
                addTask(session, description.isEmpty() ? null : new Todo(description));
                return true;
            }
        },

        /** Adds a deadline task. */
        DEADLINE {
            @Override
            boolean execute(Session session, String input) {
                String[] parts = input.substring("deadline".length()).trim().split(" /by ", 2);
                Task task = parts.length == 2 && !parts[0].isBlank() && !parts[1].isBlank()
                        ? new Deadline(parts[0].trim(), parts[1].trim()) : null;
                addTask(session, task);
                return true;
            }
        },

        /** Adds an event task. */
        EVENT {
            @Override
            boolean execute(Session session, String input) {
                String[] parts = input.substring("event".length()).trim().split(" /from ", 2);
                Task task = null;
                if (parts.length == 2 && !parts[0].isBlank()) {
                    String[] times = parts[1].split(" /to ", 2);
                    if (times.length == 2 && !times[0].isBlank() && !times[1].isBlank()) {
                        task = new Event(parts[0].trim(), times[0].trim(), times[1].trim());
                    }
                }
                addTask(session, task);
                return true;
            }
        },

        /** Ignores an unrecognised command without adding a task. */
        UNKNOWN {
            @Override
            boolean execute(Session session, String input) {
                System.out.println(" Please use todo, deadline, or event to add a task.");
                return true;
            }
        };

        /** Adds a parsed task or prints a usage message when parsing failed. */
        private static void addTask(Session session, Task task) {
            if (task == null) {
                System.out.println(" Please provide all required task details.");
            } else if (session.taskCount < MAX_TASKS) {
                session.tasks[session.taskCount] = task;
                session.taskCount++;
                System.out.println(" Got it. I've added this task:");
                System.out.println("   " + task);
            } else {
                System.out.println(" Sorry, your task list is full.");
            }
        }

        /**
         * Performs this command's task.
         *
         * @param session the session whose state may be read or updated
         * @param input the complete line entered by the user
         * @return {@code true} when the application should continue running
         */
        abstract boolean execute(Session session, String input);

        /**
         * Converts user input into a command and dispatches it through a switch.
         * Only the three supported task commands can add tasks.
         *
         * @param input the complete line entered by the user
         * @param session the current session
         * @return {@code true} when the application should continue running
         */
        static boolean executeCommand(String input, Session session) {
            Command selectedCommand = switch (input) {
                case "bye" -> BYE;
                case "list" -> LIST;
                case String command when command.startsWith("mark ") -> MARK;
                case String command when command.startsWith("unmark ") -> UNMARK;
                case "todo" -> TODO;
                case String command when command.startsWith("todo ") -> TODO;
                case "deadline" -> DEADLINE;
                case String command when command.startsWith("deadline ") -> DEADLINE;
                case "event" -> EVENT;
                case String command when command.startsWith("event ") -> EVENT;
                default -> UNKNOWN;
            };
            return selectedCommand.execute(session, input);
        }
    }

    /** Stores the mutable state shared by command actions during one session. */
    private static final class Session {
        /** Tasks in the order in which they were entered. */
        private final Task[] tasks = new Task[MAX_TASKS];

        /** Number of occupied elements in {@link #tasks}. */
        private int taskCount;

        /** Creates an empty task session. */
        private Session() {
        }
    }

    /**
     * Starts the Zabud command-line application and processes input until EOF
     * or the {@code bye} command is received.
     *
     * @param args command-line arguments; not used by this application
     */
    public static void main(String[] args) {
        String banner =
            "███████╗  █████╗  ██████╗  ██╗   ██╗ ██████╗\n" +
            "╚══███╔╝ ██╔══██╗ ██╔══██╗ ██║   ██║ ██╔══██╗\n" +
            "  ███╔╝  ███████║ ██████╔╝ ██║   ██║ ██║  ██║\n" +
            " ███╔╝   ██╔══██║ ██╔══██╗ ██║   ██║ ██║  ██║\n" +
            "███████╗ ██║  ██║ ██████╔╝ ╚██████╔╝ ██████╔╝\n" +
            "╚══════╝ ╚═╝  ╚═╝ ╚═════╝   ╚═════╝  ╚═════╝";

        Session session = new Session();

        System.out.println(SEPARATOR);
        System.out.println(banner);
        System.out.println("Hello, King Solomon! I'm Zabud, your royal assistant.");
        System.out.println("What can I do for you?");
        System.out.println(SEPARATOR);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(SEPARATOR);

            if (!Command.executeCommand(command, session)) {
                break;
            }
        }
    }

    /**
     * Prints all tasks and their completion status.
     *
     * @param session the current session containing tasks and completion states
     */
    private static void printTasks(Session session) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < session.taskCount; i++) {
            Task task = session.tasks[i];
            System.out.println(" " + (i + 1) + "." + task);
        }
    }

}
