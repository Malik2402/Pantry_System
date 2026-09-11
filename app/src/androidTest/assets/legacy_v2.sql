CREATE TABLE pantry_items (id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL CHECK(length(trim(name)) > 0),quantity TEXT NOT NULL CHECK(CAST(quantity AS REAL) > 0),unit TEXT NOT NULL CHECK(length(trim(unit)) > 0), expiry TEXT);
CREATE TABLE recipes (id INTEGER PRIMARY KEY,name TEXT NOT NULL UNIQUE, steps TEXT NOT NULL);
CREATE TABLE recipe_ingredients (id INTEGER PRIMARY KEY AUTOINCREMENT,recipe_id INTEGER NOT NULL REFERENCES recipes(id) ON DELETE CASCADE,name TEXT NOT NULL CHECK(length(trim(name)) > 0),quantity TEXT NOT NULL CHECK(CAST(quantity AS REAL) > 0), unit TEXT NOT NULL);
CREATE INDEX recipe_ingredient_parent ON recipe_ingredients(recipe_id);
INSERT INTO recipes VALUES (1,'Tomato and onion salad','1. Chop the tomatoes and onion.
2. Toss with the olive oil and serve.');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (1,'tomato','2','item');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (1,'onion','1','item');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (1,'olive oil','15','ml');
INSERT INTO recipes VALUES (2,'Simple omelette','1. Beat the eggs.
2. Warm the olive oil in a non-stick pan.
3. Add the eggs and cook gently until set, then fold.');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (2,'egg','2','item');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (2,'olive oil','5','ml');
INSERT INTO recipes VALUES (3,'Banana smoothie','1. Peel and slice the banana.
2. Blend with the milk until smooth and serve.');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (3,'banana','1','item');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (3,'milk','250','ml');
INSERT INTO recipes VALUES (4,'Creamy oats','1. Combine oats and milk in a saucepan.
2. Simmer gently for 5 minutes, stirring until thick.');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (4,'oats','50','g');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (4,'milk','250','ml');
INSERT INTO recipes VALUES (5,'Peanut butter banana toast','1. Toast the bread.
2. Spread with peanut butter and top with sliced banana.');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (5,'bread','2','piece');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (5,'peanut butter','30','g');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (5,'banana','1','item');
INSERT INTO recipes VALUES (6,'Tomato pasta','1. Boil the water and cook the pasta following its packet timing. Drain.
2. Chop the tomatoes and garlic. Soften in olive oil for 8 minutes.
3. Toss the pasta with the tomato sauce.');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (6,'pasta','150','g');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (6,'water','1000','ml');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (6,'tomato','3','item');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (6,'garlic','5','g');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (6,'olive oil','15','ml');
INSERT INTO recipes VALUES (7,'Egg fried rice','1. Beat the egg. Warm the oil in a pan and scramble the egg.
2. Add the cooked rice and peas. Stir-fry until piping hot throughout.');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (7,'cooked rice','200','g');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (7,'egg','1','item');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (7,'peas','50','g');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (7,'olive oil','15','ml');
INSERT INTO recipes VALUES (8,'Potato and onion hash','1. Dice the potatoes small and slice the onion.
2. Heat the oil, add the potatoes and cover. Cook gently, turning often, until tender.
3. Add the onion and fry until softened and golden.');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (8,'potato','300','g');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (8,'onion','1','item');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (8,'olive oil','15','ml');
INSERT INTO recipes VALUES (9,'Carrot soup','1. Chop the carrots and onion. Soften the onion in the oil.
2. Add carrots and water. Simmer for 20 minutes until tender.
3. Mash or blend until smooth.');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (9,'carrot','300','g');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (9,'onion','1','item');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (9,'olive oil','10','ml');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (9,'water','500','ml');
INSERT INTO recipes VALUES (10,'Lentil tomato stew','1. Chop the tomato and onion. Rinse the red lentils.
2. Combine all ingredients in a saucepan.
3. Simmer for 25 minutes, stirring, until the lentils are soft.');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (10,'red lentils','150','g');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (10,'tomato','2','item');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (10,'onion','1','item');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (10,'water','600','ml');
INSERT INTO recipes VALUES (11,'Chickpea cucumber salad','1. Dice the cucumber and tomatoes.
2. Toss with drained cooked chickpeas, lemon juice and olive oil.');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (11,'cooked chickpeas','200','g');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (11,'cucumber','1','item');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (11,'tomato','2','item');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (11,'lemon juice','15','ml');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (11,'olive oil','15','ml');
INSERT INTO recipes VALUES (12,'Cheese toastie','1. Place the cheese between the bread pieces.
2. Butter the outside of the sandwich.
3. Toast in a pan on both sides until golden and the cheese melts.');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (12,'bread','2','piece');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (12,'cheese','50','g');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (12,'butter','10','g');
INSERT INTO recipes VALUES (13,'Spinach scrambled eggs','1. Melt the butter and wilt the spinach.
2. Beat the eggs and add to the pan.
3. Stir gently until the eggs are fully set.');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (13,'spinach','50','g');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (13,'egg','2','item');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (13,'butter','10','g');
INSERT INTO recipes VALUES (14,'Apple yoghurt bowl','1. Core and chop the apple.
2. Spoon yoghurt into a bowl and top with the apple and oats.');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (14,'apple','1','item');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (14,'plain yoghurt','150','g');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (14,'oats','20','g');
INSERT INTO recipes VALUES (15,'Banana pancakes','1. Mash the banana. Mix with the egg and flour.
2. Warm the oil in a pan.
3. Spoon in small pancakes and cook both sides until golden and set in the centre.');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (15,'banana','1','item');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (15,'egg','1','item');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (15,'flour','40','g');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (15,'olive oil','5','ml');
INSERT INTO recipes VALUES (16,'Garlic mushrooms','1. Slice the mushrooms and finely chop the garlic.
2. Melt the butter in a pan and add the mushrooms.
3. Fry until tender, then add garlic and cook for 1 minute.');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (16,'mushroom','200','g');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (16,'garlic','5','g');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (16,'butter','15','g');
INSERT INTO recipes VALUES (17,'Cabbage carrot slaw','1. Shred the cabbage and grate the carrot.
2. Mix the yoghurt and lemon juice.
3. Toss everything together.');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (17,'cabbage','150','g');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (17,'carrot','100','g');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (17,'plain yoghurt','50','g');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (17,'lemon juice','10','ml');
INSERT INTO recipes VALUES (18,'Pea rice','1. Rinse the dry rice.
2. Add rice and water to a saucepan, cover and simmer until almost tender.
3. Stir in the peas and cook until rice and peas are tender and the water is absorbed.');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (18,'rice','150','g');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (18,'peas','100','g');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (18,'water','350','ml');
INSERT INTO recipes VALUES (19,'Honey yoghurt','1. Spoon the plain yoghurt into a bowl.
2. Stir in the honey and serve.');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (19,'plain yoghurt','150','g');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (19,'honey','2','tsp');
INSERT INTO recipes VALUES (20,'Roasted sweet potato','1. Heat the oven to 200°C.
2. Cut the sweet potato into small wedges and coat with olive oil.
3. Roast for 25–35 minutes, turning once, until tender.');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (20,'sweet potato','300','g');
INSERT INTO recipe_ingredients(recipe_id,name,quantity,unit) VALUES (20,'olive oil','1','tbsp');
INSERT INTO pantry_items VALUES (77,'Tomato','1.25','kg','2026-09-30');
PRAGMA user_version=2;