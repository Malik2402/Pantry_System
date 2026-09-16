package za.co.smartpantry.adapter;

import za.co.smartpantry.R;

public final class RecipeArtwork {
    private RecipeArtwork() {}

    public static int forRecipe(long id) {
        int art = R.drawable.home_food;
        if (id == 2 || id == 13 || id == 28) art = R.drawable.home_food_eggs;
        else if (id == 3 || id == 25) art = R.drawable.home_food_smoothie;
        else if (id == 5 || id == 12 || id == 15 || id == 21 || id == 22 || id == 27 || id == 32) art = R.drawable.home_food_bread;
        else if (id == 4 || id == 7 || id == 14 || id == 18 || id == 19 || id == 23 || id == 24 || id == 30 || id == 34 || id == 40)
            art = R.drawable.home_food_grains;
        return art;
    }
}
