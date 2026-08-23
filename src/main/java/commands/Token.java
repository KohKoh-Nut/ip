package commands;

import java.util.List;
import java.util.stream.Collectors;

/** Base class for command tokens that validate and convert one input value. */
public abstract class Token implements Validatable {
    /** Token name displayed in validation guidance. */
    protected String name;
    /** Raw value supplied after the token. */
    protected final String value;

    /** Creates a token with a name and raw value. */
    protected Token(String name, String value) {
        this.name = name.startsWith("/") ? name.substring(1) : name;
        this.value = value.trim();
    }

    /**
     * Builds the token portion appended to a command hint.
     *
     * @param tokens tokens in command syntax order
     * @return token names and their value hints, separated by spaces
     */
    public static String composeHints(List<? extends Token> tokens) {
        return tokens.stream()
                .map(token -> "/" + token.name + " " + token.hint())
                .collect(Collectors.joining(" "));
    }

    /** Renames this token for a command-specific hint. @param name the new name @return this token */
    public Token rename(String name) {
        this.name = name.startsWith("/") ? name.substring(1) : name;
        return this;
    }
}
