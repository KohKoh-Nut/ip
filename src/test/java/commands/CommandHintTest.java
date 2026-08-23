package commands;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import commands.impl.HelpCommand;
import session.Session;

/** Regression checks for command usage hints and the help requirements footer. */
public final class CommandHintTest {
    private CommandHintTest() {
    }

    /** Verifies description, task-number, unknown-command, and help guidance. */
    public static void main(String[] args) throws Exception {
        Session session = new Session(Files.createTempDirectory("zabud-hint").resolve("session.txt"));
        assert Parser.hint(Parser.parse("todo")).contains("todo DESCRIPTION");
        assert Parser.hint(Parser.parse("todo")).contains("- DESCRIPTION:");
        assert Parser.hint(Parser.parse("delete")).contains("delete TASK_NUMBER");
        assert Parser.hint(Parser.parse("delete")).contains("- TASK_NUMBER:");
        assert Parser.hint(Parser.parse("unknown")).contains("Use 'help'");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream original = System.out;
        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            List<ParsedToken> helpInput = Parser.parse("help");
            assert Parser.check(helpInput, session);
            ((HelpCommand) Parser.build(helpInput, session)).execute();
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
