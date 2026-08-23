package commands.impl;

import java.util.List;

import commands.Buildable;
import commands.Command;
import commands.ParsedToken;
import commands.Validatable;
import session.Session;
import tasks.Task;

/** Validates and builds commands that remove tasks. */
public final class DeleteCommand implements Validatable, Buildable {
    /** The user input that selects this command type. */
    public static final String COMMAND = "delete";

    /** Creates a delete command handler. */
    public DeleteCommand() {
    }

    /** {@inheritDoc} */
    @Override
    public boolean check(List<ParsedToken> tokens, Session session) {
        return Validatable.hasExistingTaskNumber(tokens, session);
    }

    /** {@inheritDoc} */
    @Override
    public String hint() {
        return Validatable.formatHint(COMMAND + " " + TASK_NUMBER, TASK_NUMBER_REQUIREMENT);
    }

    /** {@inheritDoc} */
    @Override
    public Command build(List<ParsedToken> tokens, Session session) {
        int taskNumber = Validatable.taskNumber(tokens);
        return new Command(session) {
            @Override
            public void execute() {
                Task task = session.getTaskList().remove(taskNumber);
                System.out.println(" Noted. I've removed this task:");
                System.out.println("   " + task);
            }
        };
    }
}
