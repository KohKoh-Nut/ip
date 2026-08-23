package commands.impl;

import java.util.List;

import commands.Command;
import commands.ParsedToken;
import commands.Parser;
import commands.TaskCommand;
import commands.Token;
import commands.tokens.DateTimeToken;
import session.Session;
import tasks.Event;

/** Adds a task with a start and end time. */
public final class EventCommand extends TaskCommand {
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
     * @param description validated task description
     * @param from validated event start
     * @param to validated event end
     * @param session the current session
     */
    private EventCommand(String description, DateTimeToken from, DateTimeToken to, Session session) {
        super(session);
        this.description = description;
        this.from = from;
        this.to = to;
    }

    /**
     * Builds an event command from parser-produced values.
     *
     * @param tokens structured values supplied after the command name
     * @param session current application session
     * @return event command containing the parsed values
     */
    public static Command build(List<ParsedToken> tokens, Session session) {
        return new EventCommand(tokens.getFirst().value(),
                new DateTimeToken(FROM_TOKEN, tokens.get(1).value()),
                new DateTimeToken(TO_TOKEN, tokens.get(2).value()), session);
    }

    /**
     * Checks the description, token names, and typed event boundaries.
     *
     * @param tokens parsed values supplied after the command name
     * @param session current application session
     * @return whether the event input is valid
     */
    public static boolean check(List<ParsedToken> tokens, Session session) {
        if (!hasTokenNames(tokens, Parser.DEFAULT_TOKEN, FROM_TOKEN, TO_TOKEN)
                || tokens.getFirst().value().isBlank()) return false;
        DateTimeToken from = new DateTimeToken(FROM_TOKEN, tokens.get(1).value());
        DateTimeToken to = new DateTimeToken(TO_TOKEN, tokens.get(2).value());
        return from.check() && to.check();
    }

    /**
     * Returns guidance for invalid event input.
     *
     * @return valid event syntax and requirements
     */
    public static String hint() {
        List<DateTimeToken> hintTokens = List.of(
                new DateTimeToken(FROM_TOKEN, ""), new DateTimeToken(TO_TOKEN, ""));
        return formatHint(COMMAND + " " + DESCRIPTION + " " + Token.composeHints(hintTokens),
                DESCRIPTION_REQUIREMENT, Token.composeRequirements(hintTokens));
    }

    /** {@inheritDoc} */
    @Override public void execute() {
        addTask(new Event(description, from.date(), from.time(), to.date(), to.time()));
    }
}
