package commands.impl;

import java.util.List;

import commands.Buildable;
import commands.Command;
import commands.ParsedToken;
import commands.Parser;
import commands.TaskCommand;
import commands.Token;
import commands.Validatable;
import commands.tokens.DateTimeToken;
import session.Session;
import tasks.Event;

/** Validates and builds commands that add event tasks. */
public final class EventCommand implements Validatable, Buildable {
    /** The user input that selects this command type. */
    public static final String COMMAND = "event";
    /** Name of the token introducing the event start. */
    private static final String FROM_TOKEN = "from";
    /** Name of the token introducing the event end. */
    private static final String TO_TOKEN = "to";

    /** Creates an event command handler. */
    public EventCommand() {
    }

    /** {@inheritDoc} */
    @Override
    public boolean check(List<ParsedToken> tokens, Session session) {
        if (!Validatable.hasTokenNames(tokens, Parser.DEFAULT_TOKEN, FROM_TOKEN, TO_TOKEN)
                || tokens.getFirst().value().isBlank()) return false;
        DateTimeToken from = new DateTimeToken(FROM_TOKEN, tokens.get(1).value());
        DateTimeToken to = new DateTimeToken(TO_TOKEN, tokens.get(2).value());
        return from.check() && to.check();
    }

    /** {@inheritDoc} */
    @Override
    public String hint() {
        List<DateTimeToken> hintTokens = List.of(
                new DateTimeToken(FROM_TOKEN, ""), new DateTimeToken(TO_TOKEN, ""));
        return Validatable.formatHint(COMMAND + " " + DESCRIPTION + " " + Token.composeHints(hintTokens),
                DESCRIPTION_REQUIREMENT, Token.composeRequirements(hintTokens));
    }

    /** {@inheritDoc} */
    @Override
    public Command build(List<ParsedToken> tokens, Session session) {
        String description = tokens.getFirst().value();
        DateTimeToken from = new DateTimeToken(FROM_TOKEN, tokens.get(1).value());
        DateTimeToken to = new DateTimeToken(TO_TOKEN, tokens.get(2).value());
        return new TaskCommand(session) {
            @Override
            public void execute() {
                addTask(new Event(description, from.date(), from.time(), to.date(), to.time()));
            }
        };
    }
}
