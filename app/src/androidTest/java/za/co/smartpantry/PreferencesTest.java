package za.co.smartpantry;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Test;
import za.co.smartpantry.data.AppPreferences;
import static org.junit.Assert.*;

public class PreferencesTest {
    @Test public void settingPersistsAcrossNewPreferenceInstances() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        AppPreferences preferences = new AppPreferences(context);
        boolean original = preferences.expiryIndicatorsEnabled();
        try {
            assertTrue(preferences.setExpiryIndicatorsEnabled(!original));
            assertEquals(!original, new AppPreferences(context).expiryIndicatorsEnabled());
        } finally {
            preferences.setExpiryIndicatorsEnabled(original);
        }
    }
}
