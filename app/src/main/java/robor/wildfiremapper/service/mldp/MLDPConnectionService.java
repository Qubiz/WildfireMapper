package robor.wildfiremapper.service.mldp;

import android.app.Service;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.RxBleDeviceServices;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import robor.wildfiremapper.WildfireMapperApplication;
import robor.wildfiremapper.data.DataManager;
import robor.wildfiremapper.di.component.DaggerServiceComponent;
import robor.wildfiremapper.di.component.ServiceComponent;
import robor.wildfiremapper.utils.AppLogger;

public class MLDPConnectionService extends Service {

    private static final String TAG = "MLDPConnectionService";

    private RxBleClient rxBleClient;
    private RxBleConnection rxBleConnection;

    private CompositeDisposable compositeDisposable;

    @Inject
    DataManager dataManager;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, MLDPConnectionService.class);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, MLDPConnectionService.class);
        context.startService(starter);
    }

    public static void stop(Context context) {
        context.stopService(new Intent(context, MLDPConnectionService.class));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceComponent component = DaggerServiceComponent.builder()
                .applicationComponent(((WildfireMapperApplication) getApplication()).getComponent())
                .build();
        component.inject(this);

        compositeDisposable = new CompositeDisposable();

        rxBleClient = RxBleClient.create(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AppLogger.d(TAG, "SyncService started");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        AppLogger.d(TAG, "SyncService stopped");
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void connect(final String macAddress) {
        RxBleDevice rxBleDevice = rxBleClient.getBleDevice(macAddress);

        Disposable connectionStateDisposable = rxBleDevice.observeConnectionStateChanges()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> AppLogger.e(TAG, "connectionStateSubscription: " + throwable.getMessage()))
                .subscribe(this::onConnectionStateChanged, throwable -> AppLogger.d(TAG, throwable.getMessage()));

        Disposable connectionDisposable = rxBleDevice.establishConnection(false)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> AppLogger.e(TAG, "establishConnection: " + throwable.getMessage()))
                .retry(2)
                .subscribe(this::onConnectionReceived, this::onConnectionFailure);

        compositeDisposable.addAll(
                connectionStateDisposable,
                connectionDisposable
        );
    }

    private void onConnectionFailure(Throwable throwable) {
        //TODO :: Implement : onConnectionFailure
    }

    private void onConnectionReceived(RxBleConnection rxBleConnection) {
        this.rxBleConnection = rxBleConnection;
        Disposable discoverServicesDisposable = rxBleConnection.discoverServices()
                .doOnError(throwable -> AppLogger.e(TAG, "discoverServices: " + throwable.getMessage()))
                .subscribe(this::onDiscoveredServicesReceived, throwable -> AppLogger.e(TAG, throwable.getMessage()));

        compositeDisposable.add(discoverServicesDisposable);
    }

    private void onDiscoveredServicesReceived(RxBleDeviceServices rxBleDeviceServices) {
        List<BluetoothGattService> gattServices = rxBleDeviceServices.getBluetoothGattServices();

        if (gattServices != null) {
            for (BluetoothGattService gattService : gattServices) {
                UUID serviceUUID = gattService.getUuid();
                if (serviceUUID.equals(MLDPConstants.UUID_MLDP_PRIVATE_SERVICE) || serviceUUID.equals(MLDPConstants.UUID_TRANSPARENT_PRIVATE_SERVICE)) {
                    BluetoothGattCharacteristic mldpDataCharacteristic = null;
                    BluetoothGattCharacteristic transparentTxDataCharacteristic = null;
                    BluetoothGattCharacteristic transparentRxDataCharacteristic = null;

                    List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
                    for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                        if (gattCharacteristic.getUuid().equals(MLDPConstants.UUID_TRANSPARENT_TX_PRIVATE_CHAR)) {
                            transparentTxDataCharacteristic = setupTxDataCharacteristic(gattCharacteristic, rxBleConnection);
                        }

                        if (gattCharacteristic.getUuid().equals(MLDPConstants.UUID_TRANSPARENT_RX_PRIVATE_CHAR)) {
                            transparentRxDataCharacteristic = setupRxDataCharacteristic(gattCharacteristic, rxBleConnection);
                        }

                        if (gattCharacteristic.getUuid().equals(MLDPConstants.UUID_MLDP_DATA_PRIVATE_CHAR)) {
                            mldpDataCharacteristic = setupMLDPDataCharacteristic(gattCharacteristic, rxBleConnection);
                        }
                    }

                    if (mldpDataCharacteristic != null) {
                        Disposable notificationDisposable = rxBleConnection.setupNotification(mldpDataCharacteristic)
                                .flatMap(observable -> observable)
                                .doOnError(throwable -> AppLogger.e(TAG,"setupNotification: " + throwable.getMessage()))
                                .subscribe();

                        compositeDisposable.add(notificationDisposable);
                    }

                    break;
                }
            }
        }
    }

    private void onConnectionStateChanged(RxBleConnection.RxBleConnectionState rxBleConnectionState) {
        //TODO :: Implement : onConnectionStateChanged
    }

    public static BluetoothGattCharacteristic setupTxDataCharacteristic(BluetoothGattCharacteristic gattCharacteristic, RxBleConnection rxBleConnection) {
        if (MLDPUtils.isCharacteristicNotifiable(gattCharacteristic)) {
            BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(MLDPConstants.UUID_CHAR_NOTIFICATION_DESCRIPTOR);
            rxBleConnection.writeDescriptor(descriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        }

        if (MLDPUtils.isCharacteristicWritable(gattCharacteristic)) {
            gattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
        }

        return gattCharacteristic;
    }

    public static BluetoothGattCharacteristic setupRxDataCharacteristic(BluetoothGattCharacteristic gattCharacteristic, RxBleConnection rxBleConnection) {
        if (MLDPUtils.isCharacteristicWritable(gattCharacteristic)) {
            gattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
        }
        return gattCharacteristic;
    }

    public static BluetoothGattCharacteristic setupMLDPDataCharacteristic(BluetoothGattCharacteristic gattCharacteristic, RxBleConnection rxBleConnection) {
        if (MLDPUtils.isCharacteristicNotifiable(gattCharacteristic)) {
            BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(MLDPConstants.UUID_CHAR_NOTIFICATION_DESCRIPTOR);
            rxBleConnection.writeDescriptor(descriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        }

        if (MLDPUtils.isCharacteristicWritable(gattCharacteristic)) {
            gattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
        }

        return gattCharacteristic;
    }
}
