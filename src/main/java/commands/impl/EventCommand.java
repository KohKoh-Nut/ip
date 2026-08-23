package commands.impl;

import java.util.List;

import commands.ParsedToken;
import commands.Parser;
import commands.TaskCommand;
import commands.Token;
import commands.tokens.DateTimeToken;
import session.Session;
import tasks.Event;

/** Adds a task with a start and end time. */
public class EventCommand extends TaskCommand {
    /** The user input that invokes this command. */
    public static final String COMMAND = "event";
    /** Name of the token introducing the event start. */
    private static final String FROM_TOKEN = "from";
    /** Name of the token introducing the event end. */
    private static final String TO_TOKEN = "to";
    /** Parsed task description. */
    private final String description;
    /** Parsed and typed event start. */
    private final DateTimeToken from;
    /** Parsed and typed event end. */
    private final DateTimeToken to;

    /**
     * Creates a command that adds an event task.
     *
     * @param tokens structured values supplied after the command name
     * @param session the current session
     */
    private EventCommand(List<ParsedToken> tokens, Session session) {
        super(tokens, session);
        description = tokens.isEmpty() ? "" : tokens.getFirst().value();
        String fromValue = tokens.size() < 2 ? "" : tokens.get(1).value();
        String toValue = tokens.size() < 3 ? "" : tokens.get(2).value();
        from = new DateTimeToken(FROM_TOKEN, fromValue);
        to = new DateTimeToken(TO_TOKEN, toValue);
    }

    /**
     * Builds an event command from parser-produced values.
     *
     * @param tokens structured values supplied after the command name
     * @param session current application session
     * @return event command containing the parsed values
     */
    public static EventCommand build(List<ParsedToken> tokens, Session session) {
        return new EventCommand(tokens, session);
    }

    /** {@inheritDoc} */
    @Override public void execute() {
        addTask(new Event(description, from.date(), from.time(), to.date(), to.time()));
    }
    /** {@inheritDoc} */
    @Override public boolean check() {
        return hasTokenNames(tokens, Parser.DEFAULT_TOKEN, FROM_TOKEN, TO_TOKEN)
                && !description.isBlank() && from.check() && to.check();
    }
    /** {@inheritDoc} */
    @Override public String hint() {
        List<DateTimeToken> hintTokens = List.of(
                new DateTimeToken(FROM_TOKEN, ""), new DateTimeToken(TO_TOKEN, ""));
        return formatHint(COMMAND + " " + DESCRIPTION + " " + Token.composeHints(hintTokens),
                DESCRIPTION_REQUIREMENT, Token.composeRequirements(hintTokens));
    }
}
