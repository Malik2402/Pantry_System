package za.co.smartpantry.model;

import java.math.BigDecimal;

public class Ingredient {
    public final String name;
    public final BigDecimal quantity;
    public final String unit;

    public Ingredient(String name, BigDecimal quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String amountLabel() {
        return quantity.stripTrailingZeros().toPlainString() + " " + unit;
    }
}
