package robor.wildfiremapper.di.component;

import dagger.Component;
import robor.wildfiremapper.di.PerService;
import robor.wildfiremapper.di.module.ServiceModule;
import robor.wildfiremapper.service.SyncService;
import robor.wildfiremapper.service.bluetooth.mldp.MLDPConnectionService;

@PerService
@Component(dependencies = ApplicationComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {

    void inject(SyncService service);
//    void inject(MLDPDataReceiverService service);
    void inject(MLDPConnectionService service);

}
