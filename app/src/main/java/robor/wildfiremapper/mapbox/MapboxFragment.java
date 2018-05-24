package robor.wildfiremapper.mapbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import robor.wildfiremapper.R;
import robor.wildfiremapper.di.ActivityScoped;

/**
 * Display a map.
 */
@ActivityScoped
public class MapboxFragment extends DaggerFragment implements MapboxContract.View, OnMapReadyCallback {

    @Inject
    MapboxContract.Presenter presenter;

    private MapboxMap mapboxMap;

    @Inject
    public MapboxFragment() {
        // Requires empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.dropView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.result(requestCode, resultCode);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.mapbox_fragment, container, false);



        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {

    }
}
