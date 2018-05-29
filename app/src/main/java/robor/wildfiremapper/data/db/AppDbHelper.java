package robor.wildfiremapper.data.db;

import com.polidea.rxandroidble2.RxBleDevice;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import robor.wildfiremapper.data.db.model.BleDevice;
import robor.wildfiremapper.data.db.model.BleDeviceDao;
import robor.wildfiremapper.data.db.model.DaoMaster;
import robor.wildfiremapper.data.db.model.DaoSession;
import robor.wildfiremapper.service.bluetooth.mldp.MLDPConnectionService;

/**
 * The job of this class is to manage the database and all data handling related to a database.
 */
@Singleton
public class AppDbHelper implements DbHelper {

    private final DaoSession daoSession;

    @Inject
    public AppDbHelper(DbOpenHelper dbOpenHelper) {
        daoSession = new DaoMaster(dbOpenHelper.getWritableDb()).newSession();
    }

    @Override
    public Observable<Long> insertBleDevice(BleDevice device) {
        return Observable.fromCallable(() -> daoSession.getBleDeviceDao().insertOrReplace(device));
    }

    @Override
    public Observable<Long> insertRxBleDevice(RxBleDevice rxBleDevice) {
        BleDevice bleDevice = new BleDevice();
        bleDevice.setId((long) rxBleDevice.getMacAddress().hashCode());
        bleDevice.setName(rxBleDevice.getName());
        bleDevice.setConnected(MLDPConnectionService.isConnected(rxBleDevice));
        return insertBleDevice(bleDevice);
    }

    @Override
    public Observable<BleDevice> removeBleDevice(long id) {
        return Observable.fromCallable(() -> {
            BleDevice bleDevice = daoSession.getBleDeviceDao().load(id);
            daoSession.getBleDeviceDao().deleteByKey(id);
            return bleDevice;
        });
    }

    @Override
    public Observable<BleDevice> removeBleDevice(String macAddress) {
        return Observable.fromCallable(() -> {
            BleDevice bleDevice = daoSession.getBleDeviceDao().queryBuilder()
                    .where(BleDeviceDao.Properties.MacAddress.eq(macAddress)).uniqueOrThrow();
            daoSession.getBleDeviceDao().deleteByKey(bleDevice.getId());
            return bleDevice;
        });
    }

    @Override
    public Observable<List<BleDevice>> removeAllBleDevices() {
        return Observable.fromCallable(() -> {
            List<BleDevice> bleDevices = daoSession.getBleDeviceDao().loadAll();
            daoSession.getBleDeviceDao().deleteAll();
            return bleDevices;
        });
    }

    @Override
    public Observable<List<BleDevice>> getAllBleDevices() {
        return Observable.fromCallable(() -> daoSession.getBleDeviceDao().loadAll());
    }

    @Override
    public Observable<BleDevice> setConnected(long id, boolean connected) {
        return Observable.fromCallable(() -> {
            BleDevice bleDevice = daoSession.getBleDeviceDao().load(id);
            bleDevice.setConnected(connected);
            daoSession.getBleDeviceDao().update(bleDevice);
            return bleDevice;
        });
    }

    @Override
    public Observable<BleDevice> setConnected(String macAddress, boolean connected) {
        return Observable.fromCallable(() -> {
            BleDevice bleDevice = daoSession.getBleDeviceDao()
                    .queryBuilder()
                    .where(BleDeviceDao.Properties.MacAddress.eq(macAddress))
                    .uniqueOrThrow();
            bleDevice.setConnected(connected);
            daoSession.getBleDeviceDao().update(bleDevice);
            return bleDevice;
        });
    }

    @Override
    public Observable<BleDevice> getConnectedDevice() {
        return Observable.fromCallable(() -> daoSession.getBleDeviceDao()
                .queryBuilder()
                .where(BleDeviceDao.Properties.Connected.eq(true)).uniqueOrThrow());
    }
}
