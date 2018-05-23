package robor.wildfiremapper;

import android.content.Context;

import com.mapbox.mapboxsdk.maps.MapView;

import com.wealthfront.magellan.BaseScreenView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mathijs de Groot on 23/05/2018.
 */
public class MapBoxView extends BaseScreenView<MapBoxScreen> {

    @BindView(R.id.mapView)
    MapView mapView;

    public MapBoxView(Context context) {
        super(context);
        inflate(context, R.layout.map, this);
        ButterKnife.bind(this);
    }

}
