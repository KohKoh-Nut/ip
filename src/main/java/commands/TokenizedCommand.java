package commands;

import java.util.List;

/** Defines how a command separates its description from its typed token values. */
public interface TokenizedCommand<T extends Token> {
    /**
     * Splits the command input into its task description and ordered tokens.
     *
     * @return the parsed description and tokens
     */
    Input<T> splitInput();

    /** Immutable result produced when a tokenized command splits its input. */
    record Input<T extends Token>(String description, List<T> tokens) {
        /** Copies the token list so parsed command input cannot be modified later. */
        public Input {
            tokens = List.copyOf(tokens);
        }
    }
}
