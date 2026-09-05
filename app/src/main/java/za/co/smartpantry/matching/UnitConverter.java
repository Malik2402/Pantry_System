package za.co.smartpantry.matching;

import java.math.BigDecimal;
import java.util.Locale;

public final class UnitConverter {
    public static final class Unit {
        public final String category;
        public final BigDecimal factor;
        private Unit(String category, String factor) {
            this.category = category;
            this.factor = new BigDecimal(factor);
        }
        public BigDecimal toBase(BigDecimal quantity) { return quantity.multiply(factor); }
    }

    private UnitConverter() {}

    public static Unit find(String label) {
        if (label == null) return null;
        switch (label.trim().toLowerCase(Locale.ROOT)) {
            case "g": case "gram": case "grams": return new Unit("mass", "1");
            case "kg": case "kilogram": case "kilograms": return new Unit("mass", "1000");
            case "ml": case "millilitre": case "millilitres": case "milliliter": case "milliliters":
                return new Unit("volume", "1");
            case "l": case "litre": case "litres": case "liter": case "liters":
                return new Unit("volume", "1000");
            case "tsp": case "teaspoon": case "teaspoons": return new Unit("volume", "5");
            case "tbsp": case "tablespoon": case "tablespoons": return new Unit("volume", "15");
            case "item": case "items": case "piece": case "pieces": case "whole":
                return new Unit("count", "1");
            default: return null;
        }
    }
}
