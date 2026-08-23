package commands.tokens;

import commands.Token;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/** Parses a date, time, or date-and-time command value into separate fields. */
public final class DateTimeToken extends Token {
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("d/M/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);
    private LocalDate date;
    private LocalTime time;
    private boolean parsed;

    /** Creates a date/time token. */
    public DateTimeToken(String name, String value) { super(name, value); }
    /** @return the parsed date, or {@code null} when absent */
    public LocalDate date() { parse(); return date; }
    /** @return the parsed time, or {@code null} when absent */
    public LocalTime time() { parse(); return time; }

    private void parse() {
        if (parsed) return;
        parsed = true;
        try {
            String[] parts = value.split(" ");
            if (parts.length == 1 && parts[0].matches("\\d{1,2}/\\d{1,2}/\\d{4}")) date = LocalDate.parse(parts[0], DATE);
            else if (parts.length == 1 && parts[0].matches("\\d{3,4}")) time = parseTime(parts[0]);
            else if (parts.length == 2 && parts[0].matches("\\d{1,2}/\\d{1,2}/\\d{4}")
                    && parts[1].matches("\\d{3,4}")) {
                date = LocalDate.parse(parts[0], DATE);
                time = parseTime(parts[1]);
            }
        } catch (DateTimeParseException | NumberFormatException exception) { date = null; time = null; }
    }

    private LocalTime parseTime(String text) {
        String normalized = text.length() == 3 ? "0" + text : text;
        return LocalTime.of(Integer.parseInt(normalized.substring(0, 2)), Integer.parseInt(normalized.substring(2)));
    }

    /** @return whether the value is a valid date, time, or date-time */
    @Override public boolean check() { parse(); return !value.isBlank() && (date != null || time != null); }
    /** @return syntax guidance for this token */
    @Override public String hint() { return " Use '" + name + " DD/MM/YYYY [HHMM]'."; }
}
