package robor.wildfiremapper.service.bluetooth;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.scan.ScanFilter;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import robor.wildfiremapper.WildfireMapperApplication;
import robor.wildfiremapper.data.DataManager;

/**
 * Created by Mathijs de Groot on 29/05/2018.
 */
public class DeviceScanService extends Service {

    @Inject
    DataManager dataManager;

    private static RxBleClient rxBleClient;

    private static Disposable scanDisposable;


    @Nullable
    @Override
    public IBinder onBind(
            Intent intent
    ) {
        return null;
    }

    public static Intent getStartIntent(
            Context context
    ) {
        return new Intent(context, DeviceScanService.class);
    }

    public static void start(
            Context context,
            long scanDurationSeconds,
            ScanSettings scanSettings,
            ScanFilter ... scanFilters
    ) {
        scanDisposable = rxBleClient.scanBleDevices(scanSettings, scanFilters)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose(DeviceScanService::onDispose)
                .doOnError(DeviceScanService::onError)
                .take(scanDurationSeconds, TimeUnit.SECONDS)
                .doOnComplete(() -> stop(context))
                .subscribe(DeviceScanService::onScanResult);
    }

    public static void stop(
            Context context
    ) {
        scanDisposable.dispose();
    }

    private static void onError(
            Throwable throwable
    ) {

    }

    private static void onDispose() {

    }

    private static void onScanResult(
            ScanResult scanResult
    ) {
        WildfireMapperApplication.getDataManager()
                .insertRxBleDevice(scanResult.getBleDevice())
                .subscribe();
    }
}
