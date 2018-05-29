package robor.wildfiremapper.service.bluetooth.mldp;

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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import robor.wildfiremapper.WildfireMapperApplication;
import robor.wildfiremapper.di.component.DaggerServiceComponent;
import robor.wildfiremapper.di.component.ServiceComponent;
import robor.wildfiremapper.utils.AppLogger;

import static robor.wildfiremapper.service.bluetooth.mldp.MLDPConstants.UUID_CHAR_NOTIFICATION_DESCRIPTOR;
import static robor.wildfiremapper.service.bluetooth.mldp.MLDPConstants.UUID_MLDP_DATA_PRIVATE_CHAR;
import static robor.wildfiremapper.service.bluetooth.mldp.MLDPConstants.UUID_TRANSPARENT_RX_PRIVATE_CHAR;
import static robor.wildfiremapper.service.bluetooth.mldp.MLDPConstants.UUID_TRANSPARENT_TX_PRIVATE_CHAR;

public class MLDPConnectionService extends Service {

    private static final String TAG = "MLDPConnectionService";

    private static RxBleClient      rxBleClient;
    private static RxBleDevice      rxBleDevice;
    private static RxBleConnection  rxBleConnection;

    private static Disposable connectionStateDisposable;
    private static Disposable dataWriteDisposable;
    private static Disposable notificationDisposable;
    private static Disposable connectionDisposable;
    private static Disposable discoverServicesDisposable;

    private static final CompositeDisposable COMPOSITE_DISPOSABLE = new CompositeDisposable();

    private static BluetoothGattCharacteristic mldpDataCharacteristic;
    private static BluetoothGattCharacteristic transparentTxDataCharacteristic;
    private static BluetoothGattCharacteristic transparentRxDataCharacteristic;

    private MLDPDataReceiverService dataReceiverService;

    /**
     * Returns an {@link Intent} with which you can use to start the service.
     *
     * @param context the context.
     * @return the start intent.
     */
    public static Intent getStartIntent(
            Context context
    ) {
        return new Intent(context, MLDPConnectionService.class);
    }

    /**
     * Starts the service.
     *
     * @param context the context
     */
    public void start(
            Context context
    ) {
        Intent starter = new Intent(context, MLDPConnectionService.class);
        context.startService(starter);
    }

    /**
     * Stops the service.
     *
     * @param context the context
     */
    public void stop(
            Context context
    ) {
        context.stopService(new Intent(context, MLDPConnectionService.class));
        COMPOSITE_DISPOSABLE.clear();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceComponent component = DaggerServiceComponent.builder()
                .applicationComponent(((WildfireMapperApplication) getApplication()).getComponent())
                .build();
        component.inject(this);

        rxBleClient = RxBleClient.create(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AppLogger.d(TAG, "MLDPConnectionService started");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        AppLogger.d(TAG, "MLDPConnectionService stopped");
        COMPOSITE_DISPOSABLE.clear();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(
            Intent intent
    ) {
        return null;
    }

    /**
     * Invoking this method creates a connection to a BLE device with the given MAC address.
     *
     * @param macAddress the mac address of the BLE device.
     */
    public static void connect(
            final String macAddress
    ) {
        rxBleDevice = rxBleClient.getBleDevice(macAddress);

        COMPOSITE_DISPOSABLE.remove(connectionStateDisposable);
        COMPOSITE_DISPOSABLE.remove(connectionDisposable);

        connectionStateDisposable = rxBleDevice.observeConnectionStateChanges()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(MLDPConnectionService::onError)
                .subscribe(MLDPConnectionService::onConnectionStateChanged, MLDPConnectionService::onError);

        connectionDisposable = rxBleDevice.establishConnection(false)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(MLDPConnectionService::onError)
                .retry(2)
                .subscribe(MLDPConnectionService::onConnectionReceived, MLDPConnectionService::onError);

        COMPOSITE_DISPOSABLE.addAll(
                connectionStateDisposable,
                connectionDisposable
        );
    }

    /**
     * This method is invoked whenever there occurs an error.
     *
     * @param throwable the error message.
     */
    private static void onError(
            Throwable throwable
    ) {
        AppLogger.e(TAG, throwable.getMessage());
    }

    /**
     * Disconnects from the given BLE device, if there is a connection.
     */
    public static void disconnect(
            RxBleDevice bleDevice
    ) {
        if(isConnected(bleDevice)) {
            if (connectionDisposable != null && !connectionDisposable.isDisposed()) {
                COMPOSITE_DISPOSABLE.remove(connectionDisposable);
                COMPOSITE_DISPOSABLE.remove(notificationDisposable);
            }
        }
    }

    /**
     * This method return whether there is a connection to the given BLE device.
     *
     * @param bleDevice the BLE device to check the connection for.
     * @return true if there is a connection to the given device.
     */
    public static boolean isConnected(
            RxBleDevice bleDevice
    ) {
        return bleDevice != null && bleDevice.getConnectionState() == RxBleConnection.RxBleConnectionState.CONNECTED;
    }

    /**
     * This method returns the currently connected device. If there is no device connected,
     * then this will return null.
     *
     * @return the current connected device or null if there is currently no device connected.
     */
    public RxBleDevice getConnectedDevice() {
        return isConnected(rxBleDevice) ? rxBleDevice : null;
    }

    /**
     * This method writes the given bytes to the current connected device.
     *
     * @param bytes the bytes to be written.
     */
    public void write(
            byte[] bytes
    ) {
        BluetoothGattCharacteristic writeDataCharacteristic = mldpDataCharacteristic;
        if (writeDataCharacteristic == null) {
            writeDataCharacteristic = transparentRxDataCharacteristic;
        } else {
            writeDataCharacteristic = mldpDataCharacteristic;
        }

        if (isConnected(getConnectedDevice())) {
            COMPOSITE_DISPOSABLE.remove(dataWriteDisposable);
            dataWriteDisposable = rxBleConnection.writeCharacteristic(writeDataCharacteristic, bytes)
                    .doOnError(MLDPConnectionService::onError)
                    .subscribe(writtenBytes -> {
                        AppLogger.d(TAG, new String(writtenBytes));
                    });
            COMPOSITE_DISPOSABLE.add(dataWriteDisposable);
        }
    }

    /**
     * This method is invoked whenever a new BLE connection is created.
     *
     * @param rxBleConnection the created connection.
     */
    private static void onConnectionReceived(
            RxBleConnection rxBleConnection
    ) {
        MLDPConnectionService.rxBleConnection = rxBleConnection;

        COMPOSITE_DISPOSABLE.remove(discoverServicesDisposable);
        discoverServicesDisposable = rxBleConnection.discoverServices()
                .doOnError(MLDPConnectionService::onError)
                .subscribe(MLDPConnectionService::onDiscoveredServicesReceived, MLDPConnectionService::onError);
        COMPOSITE_DISPOSABLE.add(discoverServicesDisposable);
    }

    /**
     * This methods is invoked whenever new services are discovered for a BLE connection.
     *
     * @param services the discovered services.
     */
    private static void onDiscoveredServicesReceived(
            RxBleDeviceServices services
    ) {
        if (!services.getBluetoothGattServices().isEmpty()) {
            for (BluetoothGattService gattService : services.getBluetoothGattServices()) {
                if (MLDPUtils.isPrivateService(gattService)) {
                    for (BluetoothGattCharacteristic gattCharacteristic : gattService.getCharacteristics()) {
                        if (gattCharacteristic.getUuid().equals(UUID_TRANSPARENT_TX_PRIVATE_CHAR)) {
                            setupTxDataCharacteristic(gattCharacteristic, rxBleConnection);
                        } else if (gattCharacteristic.getUuid().equals(UUID_TRANSPARENT_RX_PRIVATE_CHAR)) {
                            setupRxDataCharacteristic(gattCharacteristic);
                        } else if (gattCharacteristic.getUuid().equals(UUID_MLDP_DATA_PRIVATE_CHAR)) {
                            setupMLDPDataCharacteristic(gattCharacteristic, rxBleConnection);

                            COMPOSITE_DISPOSABLE.remove(notificationDisposable);
                            notificationDisposable = rxBleConnection.setupNotification(mldpDataCharacteristic)
                                    .flatMap(observable -> observable)
                                    .doOnError(MLDPConnectionService::onError)
                                    .subscribe(/* TODO PASS DATA TO THE RECEIVER */);
                            COMPOSITE_DISPOSABLE.add(notificationDisposable);
                        }
                    }
                    break;
                }
            }
        }
    }

    /**
     * This method is invoked when there is a connection state change. The logic for
     * handling connection state changes happens here.
     *
     * @param state the connection state.
     */
    private static void onConnectionStateChanged(
            RxBleConnection.RxBleConnectionState state
    ) {
        switch (state) {
            case CONNECTED:
                break;
            case CONNECTING:
                break;
            case DISCONNECTED:
                break;
            case DISCONNECTING:
                break;
        }
    }

    /**
     * Sets the write type for the given transparent tx data characteristic
     * to WRITE_TYPE_NO_RESPONSE and enables notifications on the given connection.
     *
     * @param gattCharacteristic    the transparent tx data characteristic.
     * @param rxBleConnection       the current ble connection.
     */
    private static void setupTxDataCharacteristic(
            BluetoothGattCharacteristic gattCharacteristic,
            RxBleConnection rxBleConnection
    ) {
        if (MLDPUtils.isCharacteristicNotifiable(gattCharacteristic)) {
            BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(UUID_CHAR_NOTIFICATION_DESCRIPTOR);
            rxBleConnection.writeDescriptor(descriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        }

        if (MLDPUtils.isCharacteristicWritable(gattCharacteristic)) {
            gattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
        }

        transparentTxDataCharacteristic = gattCharacteristic;
    }

    /**
     * Sets the write type for the given transparent rx data characteristic
     * to WRITE_TYPE_NO_RESPONSE.
     *
     * @param gattCharacteristic    the transparent rx data characteristic.
     */
    private static void setupRxDataCharacteristic(
            BluetoothGattCharacteristic gattCharacteristic
    ) {
        if (MLDPUtils.isCharacteristicWritable(gattCharacteristic)) {
            gattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
        }

        transparentRxDataCharacteristic = gattCharacteristic;
    }

    /**
     * Sets the write type for the given MLDP data characteristic
     * to WRITE_TYPE_NO_RESPONSE and enables notifications on the given connection.
     *
     * @param gattCharacteristic    the mldp data characteristic.
     * @param rxBleConnection       the current ble connection.
     */
    private static void setupMLDPDataCharacteristic(
            BluetoothGattCharacteristic gattCharacteristic,
            RxBleConnection rxBleConnection
    ) {
        if (MLDPUtils.isCharacteristicNotifiable(gattCharacteristic)) {
            BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(UUID_CHAR_NOTIFICATION_DESCRIPTOR);
            rxBleConnection.writeDescriptor(descriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        }

        if (MLDPUtils.isCharacteristicWritable(gattCharacteristic)) {
            gattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
        }

        mldpDataCharacteristic = gattCharacteristic;
    }
}
