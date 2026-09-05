package za.co.smartpantry.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public final class PantryValidation {
    public static final List<String> UNITS = java.util.Collections.unmodifiableList(
            Arrays.asList("g", "kg", "ml", "l", "tsp", "tbsp", "item", "piece", "whole"));

    private PantryValidation() {}

    public static String nameError(String value) {
        if (value == null || value.trim().isEmpty()) return "Enter an ingredient name.";
        return value.trim().length() > 80 ? "Use at most 80 characters." : null;
    }

    public static String quantityError(String value) {
        if (value == null || !value.trim().matches("[0-9]+(\\.[0-9]{1,6})?"))
            return "Enter a number with up to 6 decimal places.";
        BigDecimal amount = new BigDecimal(value.trim());
        if (amount.signum() <= 0) return "Quantity must be greater than zero.";
        if (amount.compareTo(new BigDecimal("1000000000")) > 0) return "Quantity must not exceed 1 billion.";
        return null;
    }

    public static String expiryError(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            if (!value.trim().matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) return "Use YYYY-MM-DD.";
            LocalDate.parse(value.trim());
            return null;
        } catch (DateTimeParseException exception) {
            return "Enter a valid date, for example 2026-09-30.";
        }
    }
}
