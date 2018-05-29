package robor.wildfiremapper.data.db;


import com.polidea.rxandroidble2.RxBleDevice;

import java.util.List;

import io.reactivex.Observable;

import robor.wildfiremapper.data.db.model.BleDevice;

/**
 * Interface implemented by the AppDbHelper that contains the methods exposed to the different
 * application components. This layer decouples any specific implementation of the DbHelper and
 * hence makes AppDbHelper as plug and play unit.
 */
public interface DbHelper {

    Observable<Long> insertBleDevice(final BleDevice device);

    Observable<Long> insertRxBleDevice(final RxBleDevice rxBleDevice);

    Observable<BleDevice> removeBleDevice(final long id);

    Observable<BleDevice> removeBleDevice(final String macAddress);

    Observable<List<BleDevice>> removeAllBleDevices();

    Observable<List<BleDevice>> getAllBleDevices();

    Observable<BleDevice> setConnected(final long id, boolean connected);

    Observable<BleDevice> setConnected(final String macAddress, boolean connected);

    Observable<BleDevice> getConnectedDevice();

}
