package za.co.smartpantry.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        this(context, "smart_pantry.db");
    }

    public DatabaseHelper(Context context, String databaseName) {
        super(context.getApplicationContext(), databaseName, null, 1);
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
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new IllegalStateException("A database migration is required.");
    }
}
