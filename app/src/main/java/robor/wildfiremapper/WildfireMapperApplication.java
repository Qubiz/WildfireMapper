package robor.wildfiremapper;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor.Level;

import javax.inject.Inject;

import robor.wildfiremapper.data.DataManager;
import robor.wildfiremapper.di.component.ApplicationComponent;
import robor.wildfiremapper.di.component.DaggerApplicationComponent;
import robor.wildfiremapper.di.module.ApplicationModule;
import robor.wildfiremapper.utils.AppLogger;

public class WildfireMapperApplication extends Application {

    @Inject
    DataManager dataManager;

    private static ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        applicationComponent.inject(this);

        AppLogger.init();

        AndroidNetworking.initialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            AndroidNetworking.enableLogging(Level.BODY);
        }
    }

    public static ApplicationComponent getComponent() {
        return applicationComponent;
    }

    public static DataManager getDataManager() {
        return applicationComponent.getDataManager();
    }
}
