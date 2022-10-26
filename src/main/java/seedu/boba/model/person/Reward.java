package seedu.boba.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.boba.commons.util.AppUtil.checkArgument;

import java.nio.charset.StandardCharsets;

/**
 * Represents a Person's reward points in bobaBot.
 * Guarantees: immutable; is valid as declared in {@link #isValidReward(String)}
 */
public class Reward {

    public static final String MESSAGE_CONSTRAINTS = "Reward points can take any NON-NEGATIVE values"
            + ", and it should not be blank";

    /*
     * The first character of the reward must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[^\\s].*";

    public final String value;
    public final String displayValue;

    /**
     * Constructs an {@code Reward}.
     *
     * @param reward A valid reward.
     */
    public Reward(String reward) {
        requireNonNull(reward);
        checkArgument(isValidReward(reward), MESSAGE_CONSTRAINTS);
        int integerValue = Integer.valueOf(reward);
        checkArgument(integerValue >= 0, MESSAGE_CONSTRAINTS);
        value = String.valueOf(integerValue);

        // Generate reward emoji
        byte[] emojiByteCode = new byte[] {(byte) 0xF0, (byte) 0x9F, (byte) 0x8E, (byte) 0x81};
        String emoji = new String(emojiByteCode, StandardCharsets.UTF_8);
        displayValue = emoji + " " + value;
    }

    /**
     * Returns true if a given string is a valid reward.
     */
    public static boolean isValidReward(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Reward // instanceof handles nulls
                && value.equals(((Reward) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
