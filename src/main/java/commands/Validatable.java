package commands;

/** Describes a reusable input component that can validate itself and explain its syntax. */
public interface Validatable {
    /**
     * Checks whether this component contains valid input.
     *
     * @return whether this component contains valid input
     */
    boolean check();

    /**
     * Returns guidance for correcting invalid input.
     *
     * @return guidance for correcting invalid input
     */
    String hint();
}
