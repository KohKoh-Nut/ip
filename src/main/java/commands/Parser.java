package commands;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
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
     * Checks the parsed values using the selected command's validator.
     *
     * @param parsed complete parser result, including the command entry
     * @param session current application session
     * @return whether the command can be built safely
     */
    public static boolean check(List<ParsedToken> parsed, Session session) {
        if (parsed.isEmpty() || !parsed.getFirst().name().equals(COMMAND_TOKEN)) return false;
        Definition definition = definition(parsed.getFirst().value());
        return definition.validator().test(arguments(parsed), session);
    }

    /**
     * Builds the command selected by a validated parser result.
     *
     * @param parsed validated parser result, including the command entry
     * @param session current application session
     * @return executable command selected by the command name
     */
    public static Command build(List<ParsedToken> parsed, Session session) {
        Definition definition = definition(parsed.getFirst().value());
        return definition.builder().build(arguments(parsed), session);
    }

    /**
     * Parses, validates, builds, and executes one line of input.
     *
     * @param input complete command line entered by the user
     * @param session current application session
     * @return whether the application should continue running
     */
    public static boolean invoke(String input, Session session) {
        List<ParsedToken> parsed = parse(input);
        if (!check(parsed, session)) {
            System.out.println(hint(parsed));
            return true;
        }
        Command command = build(parsed, session);
        command.execute();
        return !command.exitsApplication();
    }

    /** Returns the values following the command entry. */
    private static List<ParsedToken> arguments(List<ParsedToken> parsed) {
        return parsed.subList(1, parsed.size());
    }

    /**
     * Returns validation guidance for a parser result.
     *
     * @param parsed complete parser result, including the command entry
     * @return guidance supplied by the selected command definition
     */
    public static String hint(List<ParsedToken> parsed) {
        if (parsed.isEmpty() || !parsed.getFirst().name().equals(COMMAND_TOKEN)) {
            return UnknownCommand.hint();
        }
        return definition(parsed.getFirst().value()).hint().get();
    }

    /** Selects the builder, validator, and hint belonging to a command name. */
    private static Definition definition(String command) {
        return switch (command) {
        case ByeCommand.COMMAND -> new Definition(ByeCommand::build, ByeCommand::check, ByeCommand::hint);
        case ListCommand.COMMAND -> new Definition(ListCommand::build, ListCommand::check, ListCommand::hint);
        case HelpCommand.COMMAND -> new Definition(HelpCommand::build, HelpCommand::check, HelpCommand::hint);
        case MarkCommand.COMMAND -> new Definition(MarkCommand::build, MarkCommand::check, MarkCommand::hint);
        case UnmarkCommand.COMMAND -> new Definition(UnmarkCommand::build, UnmarkCommand::check,
                UnmarkCommand::hint);
        case DeleteCommand.COMMAND -> new Definition(DeleteCommand::build, DeleteCommand::check,
                DeleteCommand::hint);
        case TodoCommand.COMMAND -> new Definition(TodoCommand::build, TodoCommand::check, TodoCommand::hint);
        case DeadlineCommand.COMMAND -> new Definition(DeadlineCommand::build, DeadlineCommand::check,
                DeadlineCommand::hint);
        case EventCommand.COMMAND -> new Definition(EventCommand::build, EventCommand::check, EventCommand::hint);
        default -> new Definition(UnknownCommand::build, (tokens, session) -> true, UnknownCommand::hint);
        };
    }

    /** Returns the index of the first whitespace character, if one exists. */
    private static int firstWhitespace(String input) {
        for (int index = 0; index < input.length(); index++) {
            if (Character.isWhitespace(input.charAt(index))) return index;
        }
        return -1;
    }

    /**
     * Associates one command name with its construction and validation behavior.
     *
     * @param builder function that constructs the executable command
     * @param validator function that checks parsed values before construction
     * @param hint guidance returned when validation fails
     */
    private record Definition(CommandBuilder builder,
            BiPredicate<List<ParsedToken>, Session> validator, Supplier<String> hint) {
    }
}
