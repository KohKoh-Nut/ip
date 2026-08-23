package commands;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import commands.impl.ByeCommand;
import commands.impl.DeadlineCommand;
import commands.impl.DeleteCommand;
import commands.impl.EventCommand;
import commands.impl.HelpCommand;
import commands.impl.ListCommand;
import commands.impl.MarkCommand;
import commands.impl.TodoCommand;
import commands.impl.UnknownCommand;
import commands.impl.UnmarkCommand;
import session.Session;

/** Converts raw command lines into ordered, named values. */
public final class Parser {
    /** Name assigned to text between the command and the first named token. */
    public static final String DEFAULT_TOKEN = "default";

    private static final String COMMAND_TOKEN = "command";
    private static final Pattern NAMED_TOKEN = Pattern.compile("(?<!\\S)/([A-Za-z][A-Za-z0-9-]*)(?=\\s|$)");

    /** Prevents instantiation of this utility class. */
    private Parser() {
    }

    /**
     * Splits a command line into its command, default value, and named values.
     * Named values begin with a slash-prefixed name, such as {@code /by}.
     *
     * @param input complete command line entered by the user
     * @return parser results in their original command-line order
     */
    public static List<ParsedToken> parse(String input) {
        String normalized = input.trim();
        int commandEnd = firstWhitespace(normalized);
        String command = commandEnd < 0 ? normalized : normalized.substring(0, commandEnd);
        String arguments = commandEnd < 0 ? "" : normalized.substring(commandEnd).trim();

        List<ParsedToken> parsed = new ArrayList<>();
        parsed.add(new ParsedToken(COMMAND_TOKEN, command));

        Matcher matcher = NAMED_TOKEN.matcher(arguments);
        int valueStart = 0;
        String name = DEFAULT_TOKEN;
        while (matcher.find()) {
            parsed.add(new ParsedToken(name, arguments.substring(valueStart, matcher.start())));
            name = matcher.group(1);
            valueStart = matcher.end();
        }
        parsed.add(new ParsedToken(name, arguments.substring(valueStart)));
        return List.copyOf(parsed);
    }

    /**
     * Builds the command selected by the first parsed value.
     * The command name is removed before the remaining values are passed to
     * the selected command class.
     *
     * @param input complete command line entered by the user
     * @param session current application session
     * @return command corresponding to the command name, or an unknown command
     */
    public static Command build(String input, Session session) {
        List<ParsedToken> parsed = parse(input);
        String command = parsed.getFirst().value();
        List<ParsedToken> tokens = parsed.subList(1, parsed.size());
        return switch (command) {
        case ByeCommand.COMMAND -> new ByeCommand(tokens, session);
        case ListCommand.COMMAND -> new ListCommand(tokens, session);
        case HelpCommand.COMMAND -> new HelpCommand(tokens, session);
        case MarkCommand.COMMAND -> MarkCommand.build(tokens, session);
        case UnmarkCommand.COMMAND -> UnmarkCommand.build(tokens, session);
        case DeleteCommand.COMMAND -> DeleteCommand.build(tokens, session);
        case TodoCommand.COMMAND -> TodoCommand.build(tokens, session);
        case DeadlineCommand.COMMAND -> DeadlineCommand.build(tokens, session);
        case EventCommand.COMMAND -> EventCommand.build(tokens, session);
        default -> new UnknownCommand(tokens, session);
        };
    }

    /** Returns the index of the first whitespace character, if one exists. */
    private static int firstWhitespace(String input) {
        for (int index = 0; index < input.length(); index++) {
            if (Character.isWhitespace(input.charAt(index))) return index;
        }
        return -1;
    }
}
