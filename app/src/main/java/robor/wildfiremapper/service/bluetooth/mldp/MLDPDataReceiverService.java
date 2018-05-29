package robor.wildfiremapper.service.bluetooth.mldp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Mathijs de Groot on 29/05/2018.
 */
public class MLDPDataReceiverService extends Service{


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
