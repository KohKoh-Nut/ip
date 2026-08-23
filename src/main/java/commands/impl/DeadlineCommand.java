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
import tasks.Deadline;

/** Validates and builds commands that add deadline tasks. */
public final class DeadlineCommand implements Validatable, Buildable {
    /** The user input that selects this command type. */
    public static final String COMMAND = "deadline";
    /** Name of the token introducing the deadline value. */
    private static final String BY_TOKEN = "by";

    /** Creates a deadline command handler. */
    public DeadlineCommand() {
    }

    /** {@inheritDoc} */
    @Override
    public boolean check(List<ParsedToken> tokens, Session session) {
        return Validatable.hasTokenNames(tokens, Parser.DEFAULT_TOKEN, BY_TOKEN)
                && !tokens.getFirst().value().isBlank()
                && new DateTimeToken(BY_TOKEN, tokens.get(1).value()).check();
    }

    /** {@inheritDoc} */
    @Override
    public String hint() {
        List<DateTimeToken> hintTokens = List.of(new DateTimeToken(BY_TOKEN, ""));
        return Validatable.formatHint(COMMAND + " " + DESCRIPTION + " " + Token.composeHints(hintTokens),
                DESCRIPTION_REQUIREMENT, Token.composeRequirements(hintTokens));
    }

    /** {@inheritDoc} */
    @Override
    public Command build(List<ParsedToken> tokens, Session session) {
        String description = tokens.getFirst().value();
        DateTimeToken by = new DateTimeToken(BY_TOKEN, tokens.get(1).value());
        return new TaskCommand(session) {
            @Override
            public void execute() {
                addTask(new Deadline(description, by.date(), by.time()));
            }
        };
    }
}
