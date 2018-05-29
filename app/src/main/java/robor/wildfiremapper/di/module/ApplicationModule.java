package robor.wildfiremapper.di.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import robor.wildfiremapper.BuildConfig;
import robor.wildfiremapper.R;
import robor.wildfiremapper.data.AppDataManager;
import robor.wildfiremapper.data.DataManager;
import robor.wildfiremapper.data.db.AppDbHelper;
import robor.wildfiremapper.data.db.DbHelper;
import robor.wildfiremapper.data.network.ApiHelper;
import robor.wildfiremapper.data.network.AppApiHelper;
import robor.wildfiremapper.data.prefs.AppPreferencesHelper;
import robor.wildfiremapper.data.prefs.PreferencesHelper;
import robor.wildfiremapper.di.ApiInfo;
import robor.wildfiremapper.di.ApplicationContext;
import robor.wildfiremapper.di.DatabaseInfo;
import robor.wildfiremapper.di.PreferenceInfo;
import robor.wildfiremapper.utils.AppConstants;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return AppConstants.DB_NAME;
    }

    @Provides
    @ApiInfo
    String provideApiKey() {
        return BuildConfig.MAPBOX_API_KEY;
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return AppConstants.PREF_NAME;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @Singleton
    DbHelper provideDbHelper(AppDbHelper appDbHelper) {
        return appDbHelper;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Provides
    @Singleton
    ApiHelper provideApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

//    TODO :: Implement : provideProtectedApiHeader
//    @Provides
//    @Singleton
//    ApiHeader.ProtectedApiHeader provideProtectedApiHeader(@ApiInfo String apiKey, PreferencesHelper preferencesHelper) {
//        return new ApiHeader.ProtectedApiHeader(
//                apiKey,
//                preferencesHelper.getCurrentUserId(),
//                preferencesHelper.getAccessToken());
//    }

    @Provides
    @Singleton
    CalligraphyConfig provideCalligraphyDefaultConfig() {
        return new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/source-sans-pro/SourceSansPro-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build();
    }
}
