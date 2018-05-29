package robor.wildfiremapper.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import robor.wildfiremapper.di.ApplicationContext;
import robor.wildfiremapper.di.PreferenceInfo;

/**
 * The job of this class is to read and write the data from android's shared preferences.
 */
@Singleton
public class AppPreferencesHelper implements PreferencesHelper {

// EXAMPLE
//    private static final String PREF_KEY_USER_LOGGED_IN_MODE = "PREF_KEY_USER_LOGGED_IN_MODE";
//    private static final String PREF_KEY_CURRENT_USER_ID = "PREF_KEY_CURRENT_USER_ID";
//    private static final String PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN";

    private final SharedPreferences sharedPreferences;

    @Inject
    public AppPreferencesHelper(@ApplicationContext Context context,
                                @PreferenceInfo String prefFileName) {
        sharedPreferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }
}
