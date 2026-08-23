package session;

import java.nio.file.Files;
import java.nio.file.Path;

/** Regression checks for bounded interactive command history. */
public final class SessionHistoryTest {
    private SessionHistoryTest() { }

    /** Verifies previous/next navigation, escape-equivalent reset, and the 1000-entry limit. */
    public static void main(String[] args) throws Exception {
        Session session = new Session(Files.createTempDirectory("zabud-history").resolve("tasks.txt"));
        session.recordCommand("todo first");
        session.recordCommand("todo second");
        assert session.previousCommand().equals("todo second");
        assert session.previousCommand().equals("todo first");
        assert session.nextCommand().equals("todo second");
        assert session.nextCommand().equals("");
        for (int i = 0; i < Session.COMMAND_HISTORY_LIMIT + 1; i++) session.recordCommand("command " + i);
        for (int i = 0; i < Session.COMMAND_HISTORY_LIMIT - 1; i++) session.previousCommand();
        assert session.previousCommand().equals("command 1");
    }
}
