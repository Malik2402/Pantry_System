package za.co.smartpantry.ui;

import android.os.Bundle;
import za.co.smartpantry.R;

public class IngredientFormActivity extends BaseActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        showScreen(R.layout.activity_placeholder, "Add ingredient");
    }
}
