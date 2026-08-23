package commands;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import commands.impl.HelpCommand;
import commands.impl.UnknownCommand;
import session.Session;

/** Regression checks for command usage hints and the help requirements footer. */
public final class CommandHintTest {
    private CommandHintTest() {
    }

    /** Verifies description, task-number, unknown-command, and help guidance. */
    public static void main(String[] args) throws Exception {
        Session session = new Session(Files.createTempDirectory("zabud-hint").resolve("session.txt"));
        assert Parser.build("todo", session).hint().contains("todo DESCRIPTION");
        assert Parser.build("todo", session).hint().contains("- DESCRIPTION:");
        assert Parser.build("delete", session).hint().contains("delete TASK_NUMBER");
        assert Parser.build("delete", session).hint().contains("- TASK_NUMBER:");
        assert Parser.build("unknown", session) instanceof UnknownCommand;
        assert Parser.build("unknown", session).hint().contains("Use 'help'");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream original = System.out;
        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            ((HelpCommand) Parser.build("help", session)).execute();
        } finally {
            System.setOut(original);
        }
        String help = output.toString(StandardCharsets.UTF_8);
        assert help.contains("deadline DESCRIPTION /by DATE_OR_TIME");
        assert help.contains("event DESCRIPTION /from DATE_OR_TIME /to DATE_OR_TIME");
        assert occurrences(help, "DATE_OR_TIME:") == 1;
    }

    private static int occurrences(String text, String value) {
        return text.split(java.util.regex.Pattern.quote(value), -1).length - 1;
    }
}
