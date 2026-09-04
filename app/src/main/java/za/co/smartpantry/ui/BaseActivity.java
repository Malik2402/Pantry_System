package za.co.smartpantry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import za.co.smartpantry.R;

public abstract class BaseActivity extends AppCompatActivity {
    protected void showScreen(int layout, String title) {
        super.setContentView(R.layout.activity_shell);
        setSupportActionBar(findViewById(R.id.toolbar));
        setTitle(title);
        FrameLayout container = findViewById(R.id.screen_container);
        getLayoutInflater().inflate(layout, container, true);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.shell), (view, windowInsets) -> {
            Insets bars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.displayCutout() | WindowInsetsCompat.Type.ime());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        Class<?> target;
        if (item.getItemId() == R.id.nav_pantry) target = PantryActivity.class;
        else if (item.getItemId() == R.id.nav_recipes) target = SuggestionsActivity.class;
        else if (item.getItemId() == R.id.nav_settings) target = SettingsActivity.class;
        else return super.onOptionsItemSelected(item);
        if (!getClass().equals(target)) {
            startActivity(new Intent(this, target).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        }
        return true;
    }
}
