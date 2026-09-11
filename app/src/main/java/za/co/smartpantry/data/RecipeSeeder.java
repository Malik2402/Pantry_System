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
        seedAdditional(db);
    }

    static void seedAdditional(SQLiteDatabase db) {
        add(db, 21, "Garlic bread",
                "1. Finely chop the garlic and mix it into softened butter.\n2. Spread over the bread.\n3. Toast under a grill until the edges are golden and the butter is bubbling.",
                new String[]{"bread|2|piece", "butter|15|g", "garlic|5|g"});
        add(db, 22, "French toast",
                "1. Beat the egg with the milk in a shallow dish.\n2. Dip both sides of each bread piece into the mixture.\n3. Melt the butter in a pan and cook the bread on both sides until golden and the egg coating is fully set.",
                new String[]{"bread|2|piece", "egg|1|item", "milk|60|ml", "butter|10|g"});
        add(db, 23, "Apple cinnamon oats",
                "1. Core and finely chop the apple.\n2. Combine all ingredients in a saucepan.\n3. Simmer gently for 7 minutes, stirring, until the oats are creamy and the apple has softened.",
                new String[]{"oats|50|g", "milk|250|ml", "apple|1|item", "ground cinnamon|1|g"});
        add(db, 24, "Peanut butter oats",
                "1. Combine the oats and milk in a saucepan.\n2. Simmer gently for 5 minutes, stirring until thick.\n3. Stir in the peanut butter until evenly mixed and serve.",
                new String[]{"oats|50|g", "milk|250|ml", "peanut butter|20|g"});
        add(db, 25, "Cocoa banana smoothie",
                "1. Peel and slice the banana.\n2. Blend the banana, milk and cocoa powder until smooth.\n3. Pour into a glass and serve.",
                new String[]{"banana|1|item", "milk|250|ml", "cocoa powder|5|g"});
        add(db, 26, "Cucumber yoghurt dip",
                "1. Grate the cucumber and squeeze out excess liquid.\n2. Finely chop the garlic.\n3. Mix the cucumber, yoghurt, lemon juice and garlic, then serve.",
                new String[]{"cucumber|1|item", "plain yoghurt|150|g", "lemon juice|10|ml", "garlic|3|g"});
        add(db, 27, "Avocado lemon toast",
                "1. Toast the bread.\n2. Remove the avocado skin and stone, then mash the flesh with the lemon juice.\n3. Spread over the toast and serve.",
                new String[]{"bread|2|piece", "avocado|1|item", "lemon juice|10|ml"});
        add(db, 28, "Tomato cheese omelette",
                "1. Chop the tomato, grate the cheese and beat the eggs.\n2. Warm the oil in a pan and soften the tomato.\n3. Pour in the eggs and cook gently until set. Sprinkle over the cheese, fold and let the cheese melt.",
                new String[]{"egg|2|item", "tomato|1|item", "cheese|30|g", "olive oil|5|ml"});
        add(db, 29, "Mushroom spinach pasta",
                "1. Boil the water and cook the pasta following its packet timing, then drain.\n2. Slice the mushrooms and finely chop the garlic. Fry the mushrooms in the oil until tender.\n3. Add garlic and spinach; stir until the spinach wilts. Toss with the pasta.",
                new String[]{"pasta|150|g", "water|1000|ml", "mushroom|150|g", "spinach|50|g", "garlic|5|g", "olive oil|15|ml"});
        add(db, 30, "Lemon chickpea rice",
                "1. Drain the cooked chickpeas.\n2. Warm the oil in a pan and stir in the cumin for 30 seconds.\n3. Add the chickpeas and cooked rice. Stir until piping hot throughout, then stir in the lemon juice.",
                new String[]{"cooked rice|200|g", "cooked chickpeas|150|g", "lemon juice|15|ml", "olive oil|10|ml", "ground cumin|2|g"});
        add(db, 31, "Potato pea curry",
                "1. Dice the potatoes and chop the onion. Soften the onion in the oil.\n2. Stir in the curry powder, potatoes and water. Cover and simmer until the potatoes are tender, about 20 minutes.\n3. Add the peas and simmer for 5 more minutes, uncovering to thicken the sauce.",
                new String[]{"potato|300|g", "peas|100|g", "onion|1|item", "curry powder|5|g", "olive oil|15|ml", "water|300|ml"});
        add(db, 32, "Black bean cheese quesadilla",
                "1. Drain and lightly mash the cooked black beans; grate the cheese.\n2. Spread the beans and cheese over one tortilla and cover with the other.\n3. Brush a pan with the oil and cook the quesadilla on both sides until golden and hot through. Cut into wedges.",
                new String[]{"tortilla|2|piece", "cooked black beans|150|g", "cheese|50|g", "olive oil|5|ml"});
        add(db, 33, "Tuna lemon pasta",
                "1. Boil the water and cook the pasta following its packet timing, then drain.\n2. Drain the canned tuna and break it into flakes.\n3. Toss the pasta with the tuna, lemon juice and olive oil; warm through and serve.",
                new String[]{"pasta|150|g", "water|1000|ml", "canned tuna|100|g", "lemon juice|15|ml", "olive oil|10|ml"});
        add(db, 34, "Cucumber tomato couscous",
                "1. Bring the water to the boil. Pour it over the instant couscous, cover and leave for 5 minutes until absorbed.\n2. Fluff the couscous with a fork and let it cool slightly.\n3. Dice the cucumber and tomatoes, then toss everything together with the lemon juice and oil.",
                new String[]{"instant couscous|100|g", "water|150|ml", "cucumber|1|item", "tomato|2|item", "lemon juice|15|ml", "olive oil|15|ml"});
        add(db, 35, "Broccoli cheese baked potato",
                "1. Heat the oven to 200°C. Prick the potatoes and bake until tender, about 45–60 minutes depending on size.\n2. Cut the broccoli into small florets. Simmer in the water in a covered saucepan until tender, then drain.\n3. Split the potatoes, fill with broccoli and grated cheese, and return to the oven until the cheese melts.",
                new String[]{"potato|300|g", "broccoli|100|g", "cheese|40|g", "water|250|ml"});
        add(db, 36, "Tomato butter bean stew",
                "1. Chop the onion, tomatoes and garlic; drain the cooked butter beans.\n2. Soften the onion in the oil, then stir in the garlic for 1 minute.\n3. Add tomatoes, beans and water. Simmer gently for 15 minutes until hot through and thickened.",
                new String[]{"cooked butter beans|200|g", "tomato|3|item", "onion|1|item", "garlic|5|g", "olive oil|15|ml", "water|150|ml"});
        add(db, 37, "Honey roasted carrots",
                "1. Heat the oven to 200°C. Cut the carrots into thin batons.\n2. Toss with the olive oil and spread on a baking tray.\n3. Roast for 20 minutes, then toss with honey and roast for 5–10 more minutes until tender.",
                new String[]{"carrot|300|g", "olive oil|10|ml", "honey|10|ml"});
        add(db, 38, "Peanut noodles",
                "1. Boil the water and cook the noodles following their packet timing. Reserve a little of the cooking water, then drain.\n2. Mix peanut butter, soy sauce and lemon juice, adding reserved cooking water a spoonful at a time to make a coating sauce.\n3. Toss the noodles with the sauce and serve.",
                new String[]{"noodles|150|g", "water|1000|ml", "peanut butter|30|g", "soy sauce|15|ml", "lemon juice|10|ml"});
        add(db, 39, "Carrot lentil soup",
                "1. Rinse the lentils and chop the carrot and onion.\n2. Soften the onion in the oil, then stir in the cumin.\n3. Add carrots, lentils and water. Simmer for 25 minutes until the carrots and lentils are soft. Mash lightly or serve chunky.",
                new String[]{"carrot|200|g", "red lentils|100|g", "onion|1|item", "ground cumin|2|g", "olive oil|10|ml", "water|600|ml"});
        add(db, 40, "Berry yoghurt bowl",
                "1. Rinse fresh berries, or thaw frozen berries according to their packet instructions.\n2. Spoon the yoghurt into a bowl and top with berries and oats.\n3. Drizzle with honey and serve.",
                new String[]{"mixed berries|100|g", "plain yoghurt|150|g", "oats|20|g", "honey|5|ml"});
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
