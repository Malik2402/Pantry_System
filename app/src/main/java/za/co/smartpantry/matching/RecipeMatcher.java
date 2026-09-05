package za.co.smartpantry.matching;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import za.co.smartpantry.model.Ingredient;
import za.co.smartpantry.model.Recipe;

public final class RecipeMatcher {
    public boolean matches(Recipe recipe, List<? extends Ingredient> pantry) {
        if (recipe == null || recipe.ingredients.isEmpty() || pantry == null) return false;
        Map<String, BigDecimal> available = totals(pantry, false);
        Map<String, BigDecimal> required = totals(recipe.ingredients, true);
        if (required == null || required.isEmpty()) return false;
        for (Map.Entry<String, BigDecimal> need : required.entrySet()) {
            BigDecimal amount = available.getOrDefault(need.getKey(), BigDecimal.ZERO);
            if (amount.compareTo(need.getValue()) < 0) return false;
        }
        return true;
    }

    private Map<String, BigDecimal> totals(List<? extends Ingredient> ingredients, boolean strict) {
        Map<String, BigDecimal> totals = new HashMap<>();
        for (Ingredient ingredient : ingredients) {
            if (ingredient == null) {
                if (strict) return null;
                continue;
            }
            String name = IngredientNormalizer.normalize(ingredient.name);
            UnitConverter.Unit unit = UnitConverter.find(ingredient.unit);
            if (name.isEmpty() || unit == null || ingredient.quantity == null || ingredient.quantity.signum() <= 0) {
                if (strict) return null;
                continue;
            }
            String key = name + "|" + unit.category;
            totals.merge(key, unit.toBase(ingredient.quantity), BigDecimal::add);
        }
        return totals;
    }
}
