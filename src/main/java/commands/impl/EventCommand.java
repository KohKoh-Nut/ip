package commands.impl;

import java.util.List;

import commands.TaskCommand;
import commands.Token;
import commands.Tokenizable;
import commands.tokens.DateTimeToken;
import session.Session;
import tasks.Event;

/** Adds a task with a start and end time. */
public class EventCommand extends TaskCommand implements Tokenizable<DateTimeToken> {
    /** The user input that invokes this command. */
    public static final String COMMAND = "event";
    /** Name of the token introducing the event start. */
    private static final String FROM_TOKEN = "from";
    /** Name of the token introducing the event end. */
    private static final String TO_TOKEN = "to";

    /**
     * Creates a command that adds an event task.
     *
     * @param input the complete line entered by the user
     * @param session the current session
     */
    public EventCommand(String input, Session session) { super(input, session); }

    /** {@inheritDoc} */
    @Override public void execute() {
        Input<DateTimeToken> parsed = splitInput();
        DateTimeToken from = parsed.tokens().get(0);
        DateTimeToken to = parsed.tokens().get(1);
        addTask(new Event(parsed.description(), from.date(), from.time(), to.date(), to.time()));
    }
    /** {@inheritDoc} */
    @Override public boolean check() {
        Input<DateTimeToken> parsed = splitInput();
        return !parsed.description().isBlank() && parsed.tokens().stream().allMatch(Token::check);
    }
    /** {@inheritDoc} */
    @Override public String hint() {
        return " Use '" + COMMAND + " DESCRIPTION "
                + Token.composeHints(List.of(token(FROM_TOKEN, ""), token(TO_TOKEN, ""))) + "'.";
    }

    /**
     * Splits the description, start value, and end value around the required tokens.
     *
     * @return the description and the ordered {@code from} and {@code to} tokens
     */
    @Override public Input<DateTimeToken> splitInput() {
        String[] descriptionAndTimes = input.substring(COMMAND.length()).trim()
                .split(" /" + FROM_TOKEN + " ", 2);
        String values = descriptionAndTimes.length == 2 ? descriptionAndTimes[1] : "";
        String[] times = values.split(" /" + TO_TOKEN + " ", 2);
        String from = times.length == 2 ? times[0] : "";
        String to = times.length == 2 ? times[1] : "";
        return new Input<>(descriptionAndTimes[0].trim(),
                List.of(token(FROM_TOKEN, from), token(TO_TOKEN, to)));
    }

    /**
     * Creates one of this command's named date/time tokens.
     *
     * @param name token name
     * @param value raw value following the token
     * @return date/time token for the supplied event boundary
     */
    private DateTimeToken token(String name, String value) {
        return new DateTimeToken(name, value);
    }
}
