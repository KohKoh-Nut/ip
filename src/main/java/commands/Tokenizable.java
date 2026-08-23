package commands;

import java.util.List;

/**
 * Defines input parsing for a command that contains named, typed tokens.
 *
 * @param <T> token type produced while parsing the command
 */
public interface Tokenizable<T extends Token> {
    /**
     * Splits the command input into its task description and ordered tokens.
     *
     * @return the parsed description and tokens
     */
    Input<T> splitInput();

    /**
     * Immutable result produced when a tokenizable command splits its input.
     *
     * @param <T> token type stored in the result
     * @param description text describing the task
     * @param tokens parsed tokens in command syntax order
     */
    record Input<T extends Token>(String description, List<T> tokens) {
        /** Copies the token list so parsed command input cannot be modified later. */
        public Input {
            tokens = List.copyOf(tokens);
        }
    }
}
