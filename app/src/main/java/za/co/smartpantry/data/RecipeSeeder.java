package za.co.smartpantry.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public final class RecipeSeeder {
    private RecipeSeeder() {}

    public static void seed(SQLiteDatabase db) {
        add(db, 1, "Tomato and onion salad",
                "1. Chop the tomatoes and onion.\n2. Toss with the olive oil and serve.",
                new String[]{"tomato|2|item", "onion|1|item", "olive oil|15|ml"});
        add(db, 2, "Simple omelette",
                "1. Beat the eggs.\n2. Warm the olive oil in a non-stick pan.\n3. Add the eggs and cook gently until set, then fold.",
                new String[]{"egg|2|item", "olive oil|5|ml"});
        add(db, 3, "Banana smoothie",
                "1. Peel and slice the banana.\n2. Blend with the milk until smooth and serve.",
                new String[]{"banana|1|item", "milk|250|ml"});
        add(db, 4, "Creamy oats",
                "1. Combine oats and milk in a saucepan.\n2. Simmer gently for 5 minutes, stirring until thick.",
                new String[]{"oats|50|g", "milk|250|ml"});
        add(db, 5, "Peanut butter banana toast",
                "1. Toast the bread.\n2. Spread with peanut butter and top with sliced banana.",
                new String[]{"bread|2|piece", "peanut butter|30|g", "banana|1|item"});
        add(db, 6, "Tomato pasta",
                "1. Boil the water and cook the pasta following its packet timing. Drain.\n2. Chop the tomatoes and garlic. Soften in olive oil for 8 minutes.\n3. Toss the pasta with the tomato sauce.",
                new String[]{"pasta|150|g", "water|1000|ml", "tomato|3|item", "garlic|5|g", "olive oil|15|ml"});
        add(db, 7, "Egg fried rice",
                "1. Beat the egg. Warm the oil in a pan and scramble the egg.\n2. Add the cooked rice and peas. Stir-fry until piping hot throughout.",
                new String[]{"cooked rice|200|g", "egg|1|item", "peas|50|g", "olive oil|15|ml"});
        add(db, 8, "Potato and onion hash",
                "1. Dice the potatoes small and slice the onion.\n2. Heat the oil, add the potatoes and cover. Cook gently, turning often, until tender.\n3. Add the onion and fry until softened and golden.",
                new String[]{"potato|300|g", "onion|1|item", "olive oil|15|ml"});
        add(db, 9, "Carrot soup",
                "1. Chop the carrots and onion. Soften the onion in the oil.\n2. Add carrots and water. Simmer for 20 minutes until tender.\n3. Mash or blend until smooth.",
                new String[]{"carrot|300|g", "onion|1|item", "olive oil|10|ml", "water|500|ml"});
        add(db, 10, "Lentil tomato stew",
                "1. Chop the tomato and onion. Rinse the red lentils.\n2. Combine all ingredients in a saucepan.\n3. Simmer for 25 minutes, stirring, until the lentils are soft.",
                new String[]{"red lentils|150|g", "tomato|2|item", "onion|1|item", "water|600|ml"});
        add(db, 11, "Chickpea cucumber salad",
                "1. Dice the cucumber and tomatoes.\n2. Toss with drained cooked chickpeas, lemon juice and olive oil.",
                new String[]{"cooked chickpeas|200|g", "cucumber|1|item", "tomato|2|item", "lemon juice|15|ml", "olive oil|15|ml"});
        add(db, 12, "Cheese toastie",
                "1. Place the cheese between the bread pieces.\n2. Butter the outside of the sandwich.\n3. Toast in a pan on both sides until golden and the cheese melts.",
                new String[]{"bread|2|piece", "cheese|50|g", "butter|10|g"});
        add(db, 13, "Spinach scrambled eggs",
                "1. Melt the butter and wilt the spinach.\n2. Beat the eggs and add to the pan.\n3. Stir gently until the eggs are fully set.",
                new String[]{"spinach|50|g", "egg|2|item", "butter|10|g"});
        add(db, 14, "Apple yoghurt bowl",
                "1. Core and chop the apple.\n2. Spoon yoghurt into a bowl and top with the apple and oats.",
                new String[]{"apple|1|item", "plain yoghurt|150|g", "oats|20|g"});
        add(db, 15, "Banana pancakes",
                "1. Mash the banana. Mix with the egg and flour.\n2. Warm the oil in a pan.\n3. Spoon in small pancakes and cook both sides until golden and set in the centre.",
                new String[]{"banana|1|item", "egg|1|item", "flour|40|g", "olive oil|5|ml"});
        add(db, 16, "Garlic mushrooms",
                "1. Slice the mushrooms and finely chop the garlic.\n2. Melt the butter in a pan and add the mushrooms.\n3. Fry until tender, then add garlic and cook for 1 minute.",
                new String[]{"mushroom|200|g", "garlic|5|g", "butter|15|g"});
        add(db, 17, "Cabbage carrot slaw",
                "1. Shred the cabbage and grate the carrot.\n2. Mix the yoghurt and lemon juice.\n3. Toss everything together.",
                new String[]{"cabbage|150|g", "carrot|100|g", "plain yoghurt|50|g", "lemon juice|10|ml"});
        add(db, 18, "Pea rice",
                "1. Rinse the dry rice.\n2. Add rice and water to a saucepan, cover and simmer until almost tender.\n3. Stir in the peas and cook until rice and peas are tender and the water is absorbed.",
                new String[]{"rice|150|g", "peas|100|g", "water|350|ml"});
        add(db, 19, "Honey yoghurt",
                "1. Spoon the plain yoghurt into a bowl.\n2. Stir in the honey and serve.",
                new String[]{"plain yoghurt|150|g", "honey|2|tsp"});
        add(db, 20, "Roasted sweet potato",
                "1. Heat the oven to 200°C.\n2. Cut the sweet potato into small wedges and coat with olive oil.\n3. Roast for 25–35 minutes, turning once, until tender.",
                new String[]{"sweet potato|300|g", "olive oil|1|tbsp"});
    }

    private static void add(SQLiteDatabase db, long id, String name, String steps, String[] ingredients) {
        ContentValues recipe = new ContentValues();
        recipe.put("id", id);
        recipe.put("name", name);
        recipe.put("steps", steps);
        db.insertOrThrow("recipes", null, recipe);
        for (String entry : ingredients) {
            String[] fields = entry.split("\\|");
            ContentValues ingredient = new ContentValues();
            ingredient.put("recipe_id", id);
            ingredient.put("name", fields[0]);
            ingredient.put("quantity", fields[1]);
            ingredient.put("unit", fields[2]);
            db.insertOrThrow("recipe_ingredients", null, ingredient);
        }
    }
}
