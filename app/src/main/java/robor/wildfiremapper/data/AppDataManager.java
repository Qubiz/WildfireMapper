package robor.wildfiremapper.data;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

import robor.wildfiremapper.data.db.DbHelper;
import robor.wildfiremapper.data.network.ApiHelper;
import robor.wildfiremapper.data.prefs.PreferencesHelper;
import robor.wildfiremapper.di.ApplicationContext;

@Singleton
public class AppDataManager implements DataManager {

    private static final String TAG = "AppDataManager";

    private final Context context;
    private final DbHelper dbHelper;
    private final PreferencesHelper preferencesHelper;
    private final ApiHelper apiHelper;

    @Inject
    public AppDataManager(@ApplicationContext Context context,
                          DbHelper dbHelper,
                          PreferencesHelper preferencesHelper,
                          ApiHelper apiHelper) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.preferencesHelper = preferencesHelper;
        this.apiHelper = apiHelper;
    }
}
