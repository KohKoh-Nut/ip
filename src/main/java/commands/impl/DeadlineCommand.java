package commands.impl;

import java.util.List;

import commands.Command;
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
     * @param description validated task description
     * @param by validated deadline value
     * @param session the current session
     */
    private DeadlineCommand(String description, DateTimeToken by, Session session) {
        super(session);
        this.description = description;
        this.by = by;
    }

    /**
     * Builds a deadline command from parser-produced values.
     *
     * @param tokens structured values supplied after the command name
     * @param session current application session
     * @return deadline command containing the parsed values
     */
    public static Command build(List<ParsedToken> tokens, Session session) {
        return new DeadlineCommand(tokens.getFirst().value(),
                new DateTimeToken(BY_TOKEN, tokens.get(1).value()), session);
    }

    /** Checks the description, token names, and typed deadline value. */
    public static boolean check(List<ParsedToken> tokens, Session session) {
        return hasTokenNames(tokens, Parser.DEFAULT_TOKEN, BY_TOKEN)
                && !tokens.getFirst().value().isBlank()
                && new DateTimeToken(BY_TOKEN, tokens.get(1).value()).check();
    }

    /** Returns guidance for invalid deadline input. */
    public static String hint() {
        List<DateTimeToken> hintTokens = List.of(new DateTimeToken(BY_TOKEN, ""));
        return formatHint(COMMAND + " " + DESCRIPTION + " " + Token.composeHints(hintTokens),
                DESCRIPTION_REQUIREMENT, Token.composeRequirements(hintTokens));
    }

    /** {@inheritDoc} */
    @Override public void execute() {
        addTask(new Deadline(description, by.date(), by.time()));
    }
}
