package session;

import java.nio.file.Files;
import java.nio.file.Path;

/** Regression checks for bounded interactive command history. */
public final class SessionHistoryTest {
    private SessionHistoryTest() { }

    /** Verifies previous/next navigation, escape-equivalent reset, and the 1000-entry limit. */
    public static void main(String[] args) throws Exception {
        Path path = Files.createTempDirectory("zabud-history").resolve("session.txt");
        Session session = new Session(path);
        session.recordCommand("todo first");
        session.recordCommand("todo second");
        assert session.previousCommand().equals("todo second");
        assert session.previousCommand().equals("todo first");
        assert session.nextCommand().equals("todo second");
        assert session.nextCommand().equals("");
        session.resetHistoryNavigation();
        assert session.previousCommand().equals("todo second");
        for (int i = 0; i < Session.COMMAND_HISTORY_LIMIT + 1; i++) session.recordCommand("command " + i);
        for (int i = 0; i < Session.COMMAND_HISTORY_LIMIT - 1; i++) session.previousCommand();
        assert session.previousCommand().equals("command 1");

        Session restored = new Session(path);
        assert restored.previousCommand().equals("command 1000");
        for (int i = 0; i < Session.COMMAND_HISTORY_LIMIT - 1; i++) restored.previousCommand();
        assert restored.previousCommand().equals("command 1");
    }
}
