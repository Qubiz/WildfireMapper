package robor.wildfiremapper.di.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import robor.wildfiremapper.WildfireMapperApplication;
import robor.wildfiremapper.data.DataManager;
import robor.wildfiremapper.di.ApplicationContext;
import robor.wildfiremapper.di.module.ApplicationModule;
import robor.wildfiremapper.service.SyncService;
import robor.wildfiremapper.service.mldp.MLDPConnectionService;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(WildfireMapperApplication app);

    void inject(SyncService service);
    void inject(MLDPDataReceiverService service);
    void inject(MLDPConnectionService service);

    @ApplicationContext
    Context context();

    Application application();

    DataManager getDataManager();
}