package robor.wildfiremapper.data;

import android.content.Context;

import com.polidea.rxandroidble2.RxBleDevice;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import robor.wildfiremapper.data.db.DbHelper;
import robor.wildfiremapper.data.db.model.BleDevice;
import robor.wildfiremapper.data.network.ApiHelper;
import robor.wildfiremapper.data.prefs.PreferencesHelper;
import robor.wildfiremapper.di.ApplicationContext;

@Singleton
public class AppDataManager implements DataManager {

    private static final String TAG = "AppDataManager";

    private final Context               context;
    private final DbHelper              dbHelper;
    private final PreferencesHelper     preferencesHelper;
    private final ApiHelper             apiHelper;

    @Inject
    public AppDataManager(
            @ApplicationContext Context context,
            DbHelper dbHelper,
            PreferencesHelper preferencesHelper,
            ApiHelper apiHelper
    ) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.preferencesHelper = preferencesHelper;
        this.apiHelper = apiHelper;
    }

    @Override
    public Observable<Long> insertBleDevice(
            BleDevice device
    ) {
        return dbHelper.insertBleDevice(device);
    }

    @Override
    public Observable<Long> insertRxBleDevice(
            RxBleDevice rxBleDevice
    ) {
        return dbHelper.insertRxBleDevice(rxBleDevice);
    }

    @Override
    public Observable<BleDevice> removeBleDevice(
            long id
    ) {
        return dbHelper.removeBleDevice(id);
    }

    @Override
    public Observable<BleDevice> removeBleDevice(
            String macAddress
    ) {
        return dbHelper.removeBleDevice(macAddress);
    }

    @Override
    public Observable<List<BleDevice>> removeAllBleDevices() {
        return dbHelper.removeAllBleDevices();
    }

    @Override
    public Observable<List<BleDevice>> getAllBleDevices() {
        return dbHelper.getAllBleDevices();
    }

    @Override
    public Observable<BleDevice> setConnected(
            long id, boolean connected
    ) {
        return dbHelper.setConnected(id, connected);
    }

    @Override
    public Observable<BleDevice> setConnected(
            String macAddress, boolean connected
    ) {
        return dbHelper.setConnected(macAddress, connected);
    }

    @Override
    public Observable<BleDevice> getConnectedDevice() {
        return dbHelper.getConnectedDevice();
    }
}
