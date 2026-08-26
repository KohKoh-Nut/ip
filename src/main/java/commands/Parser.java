package commands;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import commands.impl.ByeCommand;
import commands.impl.DeadlineCommand;
import commands.impl.DeleteCommand;
import commands.impl.EventCommand;
import commands.impl.FindCommand;
import commands.impl.HelpCommand;
import commands.impl.ListCommand;
import commands.impl.MarkCommand;
import commands.impl.TodoCommand;
import commands.impl.UnknownCommand;
import commands.impl.UnmarkCommand;
import session.Session;

/**
 * Converts raw command lines into ordered, named values.
 */
public final class Parser {
    /**
     * Name assigned to text between the command and the first named token.
     */
    public static final String DEFAULT_TOKEN = "default";

    private static final String COMMAND_TOKEN = "command";
    private static final Pattern NAMED_TOKEN = Pattern.compile("(?<!\\S)/([A-Za-z][A-Za-z0-9-]*)(?=\\s|$)");
    private static final Handler BYE = handler(new ByeCommand());
    private static final Handler DEADLINE = handler(new DeadlineCommand());
    private static final Handler DELETE = handler(new DeleteCommand());
    private static final Handler EVENT = handler(new EventCommand());
    private static final Handler FIND = handler(new FindCommand());
    private static final Handler HELP = handler(new HelpCommand());
    private static final Handler LIST = handler(new ListCommand());
    private static final Handler MARK = handler(new MarkCommand());
    private static final Handler TODO = handler(new TodoCommand());
    private static final Handler UNKNOWN = handler(new UnknownCommand());
    private static final Handler UNMARK = handler(new UnmarkCommand());

    /**
     * Prevents instantiation of this utility class.
     */
    private Parser() {
    }

    /**
     * Splits a command line into its command, default value, and named values.
     * Named values begin with a slash-prefixed name, such as {@code /by}.
     *
     * @param input complete command line entered by the user.
     * @return parser results in their original command-line order.
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
     * @param parsed complete parser result, including the command entry.
     * @param session current application session.
     * @return whether the command can be built safely.
     */
    public static boolean check(List<ParsedToken> parsed, Session session) {
        if (parsed.isEmpty() || !parsed.getFirst().name().equals(COMMAND_TOKEN)) return false;
        Handler handler = handler(parsed.getFirst().value());
        return handler.validatable().check(arguments(parsed), session);
    }

    /**
     * Builds the command selected by a validated parser result.
     *
     * @param parsed validated parser result, including the command entry.
     * @param session current application session.
     * @return executable command selected by the command name.
     */
    public static Command build(List<ParsedToken> parsed, Session session) {
        Handler handler = handler(parsed.getFirst().value());
        return handler.buildable().build(arguments(parsed), session);
    }

    /**
     * Parses, validates, builds, and executes one line of input.
     *
     * @param input complete command line entered by the user.
     * @param session current application session.
     * @return whether the application should continue running.
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

    /**
     * Returns the values following the command entry.
     */
    private static List<ParsedToken> arguments(List<ParsedToken> parsed) {
        return parsed.subList(1, parsed.size());
    }

    /**
     * Returns validation guidance for a parser result.
     *
     * @param parsed complete parser result, including the command entry.
     * @return guidance supplied by the selected command definition.
     */
    public static String hint(List<ParsedToken> parsed) {
        if (parsed.isEmpty() || !parsed.getFirst().name().equals(COMMAND_TOKEN)) {
            return UNKNOWN.validatable().hint();
        }
        return handler(parsed.getFirst().value()).validatable().hint();
    }

    /**
     * Selects the command handler belonging to a command name.
     */
    private static Handler handler(String command) {
        return switch (command) {
        case ByeCommand.COMMAND -> BYE;
        case DeadlineCommand.COMMAND -> DEADLINE;
        case DeleteCommand.COMMAND -> DELETE;
        case EventCommand.COMMAND -> EVENT;
        case FindCommand.COMMAND -> FIND;
        case HelpCommand.COMMAND -> HELP;
        case ListCommand.COMMAND -> LIST;
        case MarkCommand.COMMAND -> MARK;
        case TodoCommand.COMMAND -> TODO;
        case UnmarkCommand.COMMAND -> UNMARK;
        default -> UNKNOWN;
        };
    }

    /**
     * Creates a parser handler from one command type implementing both contracts.
     */
    private static <T extends Validatable & Buildable> Handler handler(T command) {
        return new Handler(command, command);
    }

    /**
     * Returns the index of the first whitespace character, if one exists.
     */
    private static int firstWhitespace(String input) {
        for (int index = 0; index < input.length(); index++) {
            if (Character.isWhitespace(input.charAt(index))) return index;
        }
        return -1;
    }

    /**
     * Associates one command type's validation and construction contracts.
     */
    private record Handler(Validatable validatable, Buildable buildable) {
    }
}
