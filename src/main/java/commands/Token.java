package commands;

/** Base class for command tokens that validate and convert one input value. */
public abstract class Token implements HintCheck {
    /** Token name displayed in validation guidance. */
    protected String name;
    /** Raw value supplied after the token. */
    protected final String value;

    /** Creates a token with a name and raw value. */
    protected Token(String name, String value) { this.name = name; this.value = value.trim(); }

    /** Renames this token for a command-specific hint. @param name the new name @return this token */
    public Token rename(String name) { this.name = name; return this; }
}
