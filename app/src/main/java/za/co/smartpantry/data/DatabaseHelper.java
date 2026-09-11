package za.co.smartpantry.data;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import za.co.smartpantry.model.PantryItem;
import za.co.smartpantry.model.Recipe;
import za.co.smartpantry.model.Ingredient;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        this(context, "smart_pantry.db");
    }

    public DatabaseHelper(Context context, String databaseName) {
        super(context.getApplicationContext(), databaseName, null, 3);
    }

    @Override public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pantry_items (id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL CHECK(length(trim(name)) > 0),"
                + "quantity TEXT NOT NULL CHECK(CAST(quantity AS REAL) > 0),"
                + "unit TEXT NOT NULL CHECK(length(trim(unit)) > 0), expiry TEXT)");
        db.execSQL("CREATE TABLE recipes (id INTEGER PRIMARY KEY,"
                + "name TEXT NOT NULL UNIQUE, steps TEXT NOT NULL)");
        db.execSQL("CREATE TABLE recipe_ingredients (id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "recipe_id INTEGER NOT NULL REFERENCES recipes(id) ON DELETE CASCADE,"
                + "name TEXT NOT NULL CHECK(length(trim(name)) > 0),"
                + "quantity TEXT NOT NULL CHECK(CAST(quantity AS REAL) > 0), unit TEXT NOT NULL)");
        db.execSQL("CREATE INDEX recipe_ingredient_parent ON recipe_ingredients(recipe_id)");
        RecipeSeeder.seed(db);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) RecipeSeeder.seed(db);
        else if (oldVersion < 3) RecipeSeeder.seedAdditional(db);
    }

    public long addPantryItem(String name, BigDecimal quantity, String unit, String expiry) {
        return getWritableDatabase().insertOrThrow("pantry_items", null,
                pantryValues(name, quantity, unit, expiry));
    }

    public List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        try (Cursor cursor = getReadableDatabase().query("recipes", null, null, null,
                null, null, "name COLLATE NOCASE")) {
            while (cursor.moveToNext()) recipes.add(readRecipe(cursor));
        }
        return recipes;
    }

    public Recipe getRecipe(long id) {
        try (Cursor cursor = getReadableDatabase().query("recipes", null, "id = ?",
                new String[]{Long.toString(id)}, null, null, null)) {
            return cursor.moveToFirst() ? readRecipe(cursor) : null;
        }
    }

    private Recipe readRecipe(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
        List<Ingredient> ingredients = new ArrayList<>();
        try (Cursor parts = getReadableDatabase().query("recipe_ingredients", null, "recipe_id = ?",
                new String[]{Long.toString(id)}, null, null, "id")) {
            while (parts.moveToNext()) ingredients.add(new Ingredient(
                    parts.getString(parts.getColumnIndexOrThrow("name")),
                    new BigDecimal(parts.getString(parts.getColumnIndexOrThrow("quantity"))),
                    parts.getString(parts.getColumnIndexOrThrow("unit"))));
        }
        return new Recipe(id, cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getString(cursor.getColumnIndexOrThrow("steps")), ingredients);
    }

    public boolean updatePantryItem(long id, String name, BigDecimal quantity, String unit, String expiry) {
        return getWritableDatabase().update("pantry_items", pantryValues(name, quantity, unit, expiry),
                "id = ?", new String[]{Long.toString(id)}) == 1;
    }

    public boolean deletePantryItem(long id) {
        return getWritableDatabase().delete("pantry_items", "id = ?",
                new String[]{Long.toString(id)}) == 1;
    }

    public PantryItem getPantryItem(long id) {
        try (Cursor cursor = getReadableDatabase().query("pantry_items", null, "id = ?",
                new String[]{Long.toString(id)}, null, null, null)) {
            return cursor.moveToFirst() ? readPantryItem(cursor) : null;
        }
    }

    public List<PantryItem> getPantryItems() {
        List<PantryItem> items = new ArrayList<>();
        try (Cursor cursor = getReadableDatabase().query("pantry_items", null, null, null,
                null, null, "name COLLATE NOCASE, id")) {
            while (cursor.moveToNext()) items.add(readPantryItem(cursor));
        }
        return items;
    }

    private PantryItem readPantryItem(Cursor cursor) {
        return new PantryItem(cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow("quantity"))),
                cursor.getString(cursor.getColumnIndexOrThrow("unit")),
                cursor.getString(cursor.getColumnIndexOrThrow("expiry")));
    }

    private ContentValues pantryValues(String name, BigDecimal quantity, String unit, String expiry) {
        if (name == null || name.trim().isEmpty() || name.trim().length() > 80)
            throw new IllegalArgumentException("Enter an ingredient name (1–80 characters).");
        if (quantity == null || quantity.signum() <= 0 || quantity.compareTo(new BigDecimal("1000000000")) > 0
                || quantity.stripTrailingZeros().scale() > 6)
            throw new IllegalArgumentException("Enter a quantity above zero, up to 1 billion, with at most 6 decimal places.");
        if (unit == null || !PantryValidation.UNITS.contains(unit))
            throw new IllegalArgumentException("Select a supported unit.");
        String date = expiry == null || expiry.trim().isEmpty() ? null : expiry.trim();
        if (date != null) LocalDate.parse(date);
        ContentValues values = new ContentValues();
        values.put("name", name.trim().replaceAll("\\s+", " "));
        values.put("quantity", quantity.stripTrailingZeros().toPlainString());
        values.put("unit", unit);
        values.put("expiry", date);
        return values;
    }
}
