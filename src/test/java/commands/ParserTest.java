package commands;

import java.util.List;

/** Regression checks for parsing raw input into ordered named values. */
public final class ParserTest {
    private ParserTest() {
    }

    /** Verifies default, named, empty, and multiple token values. */
    public static void main(String[] args) {
        assert Parser.parse("deadline test /by 1200").equals(List.of(
                new ParsedToken("command", "deadline"),
                new ParsedToken("default", "test"),
                new ParsedToken("by", "1200")));

        assert Parser.parse("event meeting /from 2/12/2019 1800 /to 2/12/2019 1900").equals(List.of(
                new ParsedToken("command", "event"),
                new ParsedToken("default", "meeting"),
                new ParsedToken("from", "2/12/2019 1800"),
                new ParsedToken("to", "2/12/2019 1900")));

        assert Parser.parse("list").equals(List.of(
                new ParsedToken("command", "list"),
                new ParsedToken("default", "")));
        assert Parser.parse("deadline test /by").getLast().equals(new ParsedToken("by", ""));
    }
}
