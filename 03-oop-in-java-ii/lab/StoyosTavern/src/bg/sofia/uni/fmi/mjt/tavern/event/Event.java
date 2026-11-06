package bg.sofia.uni.fmi.mjt.tavern.event;

public sealed interface Event permits Drink, Insult, Cheat, Pay, Leave {
    static void validateString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Invalid string");
        }
    }

    static void validateNumber(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Number must be positive");
        }
    }

    static void validateDifferentString(String first, String second) {
        validateString(first);
        validateString(second);

        if (first.equals(second)) {
            throw new IllegalArgumentException("Strings must be different");
        }
    }
}