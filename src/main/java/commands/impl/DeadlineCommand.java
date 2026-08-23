package commands.impl;

import java.util.List;

import commands.ParsedToken;
import commands.Parser;
import commands.TaskCommand;
import commands.Token;
import commands.tokens.DateTimeToken;
import session.Session;
import tasks.Deadline;

/** Adds a task with a deadline. */
public class DeadlineCommand extends TaskCommand {
    /** The user input that invokes this command. */
    public static final String COMMAND = "deadline";
    /** Tokens required after the command name. */
    private static final String BY_TOKEN = "by";
    /** Parsed task description. */
    private final String description;
    /** Parsed and typed deadline value. */
    private final DateTimeToken by;

    /**
     * Creates a command that adds a deadline task.
     *
     * @param tokens structured values supplied after the command name
     * @param session the current session
     */
    private DeadlineCommand(List<ParsedToken> tokens, Session session) {
        super(tokens, session);
        description = tokens.isEmpty() ? "" : tokens.getFirst().value();
        String value = tokens.size() < 2 ? "" : tokens.get(1).value();
        by = new DateTimeToken(BY_TOKEN, value);
    }

    /**
     * Builds a deadline command from parser-produced values.
     *
     * @param tokens structured values supplied after the command name
     * @param session current application session
     * @return deadline command containing the parsed values
     */
    public static DeadlineCommand build(List<ParsedToken> tokens, Session session) {
        return new DeadlineCommand(tokens, session);
    }

    /** {@inheritDoc} */
    @Override public void execute() {
        addTask(new Deadline(description, by.date(), by.time()));
    }
    /** {@inheritDoc} */
    @Override public boolean check() {
        return hasTokenNames(tokens, Parser.DEFAULT_TOKEN, BY_TOKEN)
                && !description.isBlank() && by.check();
    }
    /** {@inheritDoc} */
    @Override public String hint() {
        List<DateTimeToken> hintTokens = List.of(new DateTimeToken(BY_TOKEN, ""));
        return formatHint(COMMAND + " " + DESCRIPTION + " " + Token.composeHints(hintTokens),
                DESCRIPTION_REQUIREMENT, Token.composeRequirements(hintTokens));
    }
}
