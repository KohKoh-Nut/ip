package commands.impl;

import commands.*;

import commands.TaskCommand;
import commands.tokens.DateTimeToken;
import tasks.Event;
import session.Session;
import java.util.List;

/** Adds a task with a start and end time. */
public class EventCommand extends TaskCommand {
    /** The user input that invokes this command. */
    public static final String COMMAND = "event";
    /** Tokens required after the command name. */
    private static final String[] REQUIRED_TOKENS = {"/from", "/to"};

    /**
     * Creates a command that adds an event task.
     *
     * @param input the complete line entered by the user
     * @param session the current session
     */
    public EventCommand(String input, Session session) { super(input, session); }

    /** {@inheritDoc} */
    @Override public void execute() {
        String[] details = details();
        DateTimeToken from = token("/from", details[1]);
        DateTimeToken to = token("/to", details[2]);
        addTask(new Event(details[0], from.date(), from.time(), to.date(), to.time()));
    }
    /** {@inheritDoc} */
    @Override public boolean check() {
        String[] details = details();
        return details != null && !details[0].isBlank()
                && token("/from", details[1]).check() && token("/to", details[2]).check();
    }
    /** {@inheritDoc} */
    @Override public String hint() {
        return " Use '" + COMMAND + " DESCRIPTION "
                + Token.composeHints(List.of(token("from", ""), token("to", ""))) + "'.";
    }

    /**
     * Splits the description, start time, and end time around the required tokens.
     *
     * @return the description, start time, and end time; {@code null} when a token is missing
     */
    private String[] details() {
        String[] descriptionAndTimes = input.substring(COMMAND.length()).trim()
                .split(" " + REQUIRED_TOKENS[0] + " ", 2);
        if (descriptionAndTimes.length != 2) return null;
        String[] times = descriptionAndTimes[1].split(" " + REQUIRED_TOKENS[1] + " ", 2);
        if (times.length != 2) return null;
        return new String[] {descriptionAndTimes[0].trim(), times[0].trim(), times[1].trim()};
    }

    private DateTimeToken token(String name, String value) { return new DateTimeToken(name, value); }
}
