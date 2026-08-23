package commands.impl;

import java.util.List;

import commands.Command;
import commands.Token;
import commands.tokens.DateTimeToken;
import session.Session;

/** Displays the commands available in Zabud. */
public class HelpCommand extends Command {
    /** The user input that invokes this command. */
    public static final String COMMAND = "help";

    /** Tokens required after the command name. */
    private static final String[] REQUIRED_TOKENS = {};

    /**
     * Creates a command that displays available command syntax.
     *
     * @param input the complete line entered by the user
     * @param session the current session
     */
    public HelpCommand(String input, Session session) {
        super(input, session);
    }

    /** {@inheritDoc} */
    @Override
    public void execute() {
        DateTimeToken by = new DateTimeToken("by", "");
        DateTimeToken from = new DateTimeToken("from", "");
        DateTimeToken to = new DateTimeToken("to", "");
        System.out.println(" Available commands:");
        System.out.println("   todo " + DESCRIPTION);
        System.out.println("   deadline " + DESCRIPTION + " " + Token.composeHints(List.of(by)));
        System.out.println("   event " + DESCRIPTION + " " + Token.composeHints(List.of(from, to)));
        System.out.println("   list");
        System.out.println("   mark " + TASK_NUMBER);
        System.out.println("   unmark " + TASK_NUMBER);
        System.out.println("   delete " + TASK_NUMBER);
        System.out.println("   help");
        System.out.println("   bye");
        System.out.println();
        System.out.println(" Details:");
        System.out.println(DESCRIPTION_REQUIREMENT);
        System.out.println(TASK_NUMBER_REQUIREMENT);
        System.out.println(Token.composeRequirements(List.of(by, from, to)));
    }

    /** {@inheritDoc} */
    @Override
    public boolean check() {
        return REQUIRED_TOKENS.length == 0;
    }

    /** {@inheritDoc} */
    @Override
    public String hint() {
        return "";
    }
}
