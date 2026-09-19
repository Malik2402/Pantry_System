package za.co.smartpantry.adapter;

import java.util.HashMap;
import java.util.Map;
import za.co.smartpantry.R;
import za.co.smartpantry.matching.IngredientNormalizer;

public final class IngredientArtwork {
    private static final Map<String, Integer> IMAGES = new HashMap<>();
    static {
        IMAGES.put(IngredientNormalizer.normalize("apple"), R.drawable.ingredient_apple);
        IMAGES.put(IngredientNormalizer.normalize("avocado"), R.drawable.ingredient_avocado);
        IMAGES.put(IngredientNormalizer.normalize("banana"), R.drawable.ingredient_banana);
        IMAGES.put(IngredientNormalizer.normalize("bread"), R.drawable.ingredient_bread);
        IMAGES.put(IngredientNormalizer.normalize("broccoli"), R.drawable.ingredient_broccoli);
        IMAGES.put(IngredientNormalizer.normalize("butter"), R.drawable.ingredient_butter);
        IMAGES.put(IngredientNormalizer.normalize("cabbage"), R.drawable.ingredient_cabbage);
        IMAGES.put(IngredientNormalizer.normalize("canned tuna"), R.drawable.ingredient_canned_tuna);
        IMAGES.put(IngredientNormalizer.normalize("carrot"), R.drawable.ingredient_carrot);
        IMAGES.put(IngredientNormalizer.normalize("cheese"), R.drawable.ingredient_cheese);
        IMAGES.put(IngredientNormalizer.normalize("cocoa powder"), R.drawable.ingredient_cocoa_powder);
        IMAGES.put(IngredientNormalizer.normalize("cooked black beans"), R.drawable.ingredient_cooked_black_beans);
        IMAGES.put(IngredientNormalizer.normalize("cooked butter beans"), R.drawable.ingredient_cooked_butter_beans);
        IMAGES.put(IngredientNormalizer.normalize("cooked chickpeas"), R.drawable.ingredient_cooked_chickpeas);
        IMAGES.put(IngredientNormalizer.normalize("cooked rice"), R.drawable.ingredient_cooked_rice);
        IMAGES.put(IngredientNormalizer.normalize("cucumber"), R.drawable.ingredient_cucumber);
        IMAGES.put(IngredientNormalizer.normalize("curry powder"), R.drawable.ingredient_curry_powder);
        IMAGES.put(IngredientNormalizer.normalize("egg"), R.drawable.ingredient_egg);
        IMAGES.put(IngredientNormalizer.normalize("flour"), R.drawable.ingredient_flour);
        IMAGES.put(IngredientNormalizer.normalize("garlic"), R.drawable.ingredient_garlic);
        IMAGES.put(IngredientNormalizer.normalize("ground cinnamon"), R.drawable.ingredient_ground_cinnamon);
        IMAGES.put(IngredientNormalizer.normalize("ground cumin"), R.drawable.ingredient_ground_cumin);
        IMAGES.put(IngredientNormalizer.normalize("honey"), R.drawable.ingredient_honey);
        IMAGES.put(IngredientNormalizer.normalize("instant couscous"), R.drawable.ingredient_instant_couscous);
        IMAGES.put(IngredientNormalizer.normalize("lemon juice"), R.drawable.ingredient_lemon_juice);
        IMAGES.put(IngredientNormalizer.normalize("milk"), R.drawable.ingredient_milk);
        IMAGES.put(IngredientNormalizer.normalize("mixed berries"), R.drawable.ingredient_mixed_berries);
        IMAGES.put(IngredientNormalizer.normalize("mushroom"), R.drawable.ingredient_mushroom);
        IMAGES.put(IngredientNormalizer.normalize("noodles"), R.drawable.ingredient_noodles);
        IMAGES.put(IngredientNormalizer.normalize("oats"), R.drawable.ingredient_oats);
        IMAGES.put(IngredientNormalizer.normalize("olive oil"), R.drawable.ingredient_olive_oil);
        IMAGES.put(IngredientNormalizer.normalize("onion"), R.drawable.ingredient_onion);
        IMAGES.put(IngredientNormalizer.normalize("pasta"), R.drawable.ingredient_pasta);
        IMAGES.put(IngredientNormalizer.normalize("peanut butter"), R.drawable.ingredient_peanut_butter);
        IMAGES.put(IngredientNormalizer.normalize("peas"), R.drawable.ingredient_peas);
        IMAGES.put(IngredientNormalizer.normalize("plain yoghurt"), R.drawable.ingredient_plain_yoghurt);
        IMAGES.put(IngredientNormalizer.normalize("potato"), R.drawable.ingredient_potato);
        IMAGES.put(IngredientNormalizer.normalize("red lentils"), R.drawable.ingredient_red_lentils);
        IMAGES.put(IngredientNormalizer.normalize("rice"), R.drawable.ingredient_rice);
        IMAGES.put(IngredientNormalizer.normalize("soy sauce"), R.drawable.ingredient_soy_sauce);
        IMAGES.put(IngredientNormalizer.normalize("spinach"), R.drawable.ingredient_spinach);
        IMAGES.put(IngredientNormalizer.normalize("sweet potato"), R.drawable.ingredient_sweet_potato);
        IMAGES.put(IngredientNormalizer.normalize("tomato"), R.drawable.ingredient_tomato);
        IMAGES.put(IngredientNormalizer.normalize("tortilla"), R.drawable.ingredient_tortilla);
        IMAGES.put(IngredientNormalizer.normalize("water"), R.drawable.ingredient_water);
    }
    private IngredientArtwork() {}

    public static int forName(String name) {
        return IMAGES.getOrDefault(IngredientNormalizer.normalize(name), R.drawable.home_jar_icon);
    }
}


