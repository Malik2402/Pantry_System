package za.co.smartpantry.model;

import java.math.BigDecimal;

public class PantryItem extends Ingredient {
    public final long id;
    public final String expiry;

    public PantryItem(long id, String name, BigDecimal quantity, String unit, String expiry) {
        super(name, quantity, unit);
        this.id = id;
        this.expiry = expiry;
    }
}
