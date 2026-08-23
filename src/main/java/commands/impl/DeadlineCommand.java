package commands.impl;

import java.util.List;

import commands.TaskCommand;
import commands.Token;
import commands.Tokenizable;
import commands.tokens.DateTimeToken;
import session.Session;
import tasks.Deadline;

/** Adds a task with a deadline. */
public class DeadlineCommand extends TaskCommand implements Tokenizable<DateTimeToken> {
    /** The user input that invokes this command. */
    public static final String COMMAND = "deadline";
    /** Tokens required after the command name. */
    private static final String BY_TOKEN = "by";

    /**
     * Creates a command that adds a deadline task.
     *
     * @param input the complete line entered by the user
     * @param session the current session
     */
    public DeadlineCommand(String input, Session session) { super(input, session); }

    /** {@inheritDoc} */
    @Override public void execute() {
        Input<DateTimeToken> parsed = splitInput();
        DateTimeToken by = parsed.tokens().getFirst();
        addTask(new Deadline(parsed.description(), by.date(), by.time()));
    }
    /** {@inheritDoc} */
    @Override public boolean check() {
        Input<DateTimeToken> parsed = splitInput();
        return !parsed.description().isBlank() && parsed.tokens().stream().allMatch(Token::check);
    }
    /** {@inheritDoc} */
    @Override public String hint() {
        return " Use '" + COMMAND + " DESCRIPTION " + Token.composeHints(List.of(token(""))) + "'.";
    }

    /**
     * Splits the description and deadline value around {@code /by}.
     *
     * @return the description and one named date/time token
     */
    @Override public Input<DateTimeToken> splitInput() {
        String[] parts = input.substring(COMMAND.length()).trim().split(" /" + BY_TOKEN + " ", 2);
        String value = parts.length == 2 ? parts[1] : "";
        return new Input<>(parts[0].trim(), List.of(token(value)));
    }

    /**
     * Creates the command's named deadline token.
     *
     * @param value raw value following {@code /by}
     * @return date/time token named {@code by}
     */
    private DateTimeToken token(String value) {
        return new DateTimeToken(BY_TOKEN, value);
    }
}
