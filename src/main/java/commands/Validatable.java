package commands;

/** Describes a reusable input component that can validate itself and explain its syntax. */
public interface Validatable {
    /** @return whether this component contains valid input */
    boolean check();

    /** @return guidance for correcting invalid input */
    String hint();
}
