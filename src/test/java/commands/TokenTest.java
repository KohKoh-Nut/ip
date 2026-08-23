package commands;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import commands.tokens.DateTimeToken;

/** Regression checks for command token temporal parsing. */
public final class TokenTest {
    private TokenTest() { }

    /** Verifies date-only, time-only, date-time, and invalid values. */
    public static void main(String[] args) {
        assert new DateTimeToken("by", "2/12/2019").date().equals(LocalDate.of(2019, 12, 2));
        assert new DateTimeToken("by", "1800").time().equals(LocalTime.of(18, 0));
        DateTimeToken both = new DateTimeToken("by", "2/12/2019 1800");
        assert both.date().equals(LocalDate.of(2019, 12, 2));
        assert both.time().equals(LocalTime.of(18, 0));
        assert !new DateTimeToken("by", "31/2/2019 1800").check();
        assert Token.composeHints(List.of(new DateTimeToken("by", "")))
                .equals("/by DD/MM/YYYY, HHMM, or DD/MM/YYYY HHMM");
    }
}
